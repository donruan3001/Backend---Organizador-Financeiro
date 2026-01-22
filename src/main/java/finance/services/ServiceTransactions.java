package finance.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import finance.config.AuthenticatedUser;
import finance.domain.acounts.Account;
import finance.domain.dto.transactions.TransactionCreateDTO;
import finance.domain.dto.transactions.TransactionResponseDTO;
import finance.domain.dto.transactions.TransactionUpdateDTO;
import finance.domain.transactions.Transaction;
import finance.domain.transactions.TypeTransaction;
import finance.exceptions.AccountNotFoundException;
import finance.exceptions.InsufficientBalanceException;
import finance.exceptions.ResourceNotFoundException;
import finance.exceptions.UnauthorizedAccessException;
import finance.repository.RepositoryAccount;
import finance.repository.RepositoryTransactions;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceTransactions {
    private  RepositoryAccount accountRepository;
    private  RepositoryTransactions transactionRepository;

    public ServiceTransactions(RepositoryAccount accountRepository, RepositoryTransactions transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }


    @Transactional
    public TransactionResponseDTO createTransaction(TransactionCreateDTO data) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Account account = accountRepository.findById(data.accountId())
                .orElseThrow(() -> new AccountNotFoundException(data.accountId()));

        if (!account.getUser().getUsername().equals(username)) {
            throw new UnauthorizedAccessException("Conta", data.accountId());
        }

        if (data.type().equals(TypeTransaction.EXPENSE)) {
            // Valida saldo suficiente antes de debitar
            if (account.getBalance().compareTo(data.amount()) < 0) {
                throw new InsufficientBalanceException(
                        account.getBalance(),
                        data.amount());
            }
            account.setBalance(account.getBalance().subtract(data.amount()));
            accountRepository.save(account);
        }

        if (data.type().equals(TypeTransaction.INCOME)) {
            account.setBalance(account.getBalance().add(data.amount()));
            accountRepository.save(account);
        }

        Transaction transaction = new Transaction(
                account,
                data.category(),
                data.name(),
                data.type(),
                data.amount());

        transaction = transactionRepository.save(transaction);

        return new TransactionResponseDTO(
                transaction.getId(),
                account.getId(),
                transaction.getCategory(),
                transaction.getName(),
                transaction.getAmount(),
                transaction.getCreated(),
                transaction.getUpdated());
    }

    public TransactionResponseDTO getTransactionById(Long id) {
        Long userId = AuthenticatedUser.getAuthenticatedUserId();
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transação", id));

        // Verify ownership through account
        if (!transaction.getAccount().getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("Transação", id);
        }

        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getAccount().getId(),
                transaction.getCategory(),
                transaction.getName(),
                transaction.getAmount(),
                transaction.getCreated(),
                transaction.getUpdated()
        );
    }

    public List<TransactionResponseDTO> getAllTransactions() {
        Long userId = AuthenticatedUser.getAuthenticatedUserId();
        List<Account> userAccounts = accountRepository.findByUserId(userId);

        return userAccounts.stream()
                .flatMap(account -> transactionRepository.findByAccountIdOrderByCreatedDesc(account.getId()).stream())
                .map(t -> new TransactionResponseDTO(
                        t.getId(),
                        t.getAccount().getId(),
                        t.getCategory(),
                        t.getName(),
                        t.getAmount(),
                        t.getCreated(),
                        t.getUpdated()
                ))
                .collect(Collectors.toList());
    }

    public List<TransactionResponseDTO> getTransactionsByAccountId(Long accountId) {
        Long userId = AuthenticatedUser.getAuthenticatedUserId();
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        if (!account.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("Conta", accountId);
        }

        return transactionRepository.findByAccountIdOrderByCreatedDesc(accountId).stream()
                .map(t -> new TransactionResponseDTO(
                        t.getId(),
                        t.getAccount().getId(),
                        t.getCategory(),
                        t.getName(),
                        t.getAmount(),
                        t.getCreated(),
                        t.getUpdated()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public TransactionResponseDTO updateTransaction(Long id, TransactionUpdateDTO data) {
        Long userId = AuthenticatedUser.getAuthenticatedUserId();
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transação", id));

        if (!transaction.getAccount().getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("Transação", id);
        }

        Account account = transaction.getAccount();

        // Revert old transaction effect
        if (transaction.getType() == TypeTransaction.EXPENSE) {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        } else if (transaction.getType() == TypeTransaction.INCOME) {
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        }

        // Update transaction fields
        if (data.name() != null) transaction.setName(data.name());
        if (data.category() != null) transaction.setCategory(data.category());
        if (data.type() != null) transaction.setType(data.type());
        if (data.amount() != null) transaction.setAmount(data.amount());

        // Apply new transaction effect
        if (transaction.getType() == TypeTransaction.EXPENSE) {
            if (account.getBalance().compareTo(transaction.getAmount()) < 0) {
                throw new InsufficientBalanceException(account.getBalance(), transaction.getAmount());
            }
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        } else if (transaction.getType() == TypeTransaction.INCOME) {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        }

        accountRepository.save(account);
        Transaction updated = transactionRepository.save(transaction);

        return new TransactionResponseDTO(
                updated.getId(),
                updated.getAccount().getId(),
                updated.getCategory(),
                updated.getName(),
                updated.getAmount(),
                updated.getCreated(),
                updated.getUpdated()
        );
    }

    @Transactional
    public void deleteTransaction(Long id) {
        Long userId = AuthenticatedUser.getAuthenticatedUserId();
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transação", id));

        if (!transaction.getAccount().getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("Transação", id);
        }

        // Revert transaction effect on account balance
        Account account = transaction.getAccount();
        if (transaction.getType() == TypeTransaction.EXPENSE) {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        } else if (transaction.getType() == TypeTransaction.INCOME) {
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        }

        accountRepository.save(account);
        transactionRepository.delete(transaction);
    }

}