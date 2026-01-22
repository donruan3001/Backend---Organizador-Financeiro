package finance.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import finance.domain.acounts.Account;
import finance.domain.dto.accounts.AccountResponseDTO;
import finance.domain.dto.accounts.AccountUpdateDTO;
import finance.exceptions.AccountNotFoundException;
import finance.exceptions.BusinessException;
import finance.exceptions.ResourceNotFoundException;
import finance.repository.RepositoryAccount;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

@Service
public class ServiceAdmin {

    private final RepositoryAccount accountRepository;

    public ServiceAdmin(RepositoryAccount accountRepository) {
        this.accountRepository = accountRepository;
    }

    // lista todas as contas
    public Page<AccountResponseDTO> getAllAccounts(Pageable pageable) {

        Page<Account> accounts = accountRepository.findAll(pageable);

        if (accounts.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma conta encontrada");
        }
        return accounts.map(AccountResponseDTO::toDTO);
    }

    // atualiza contas
    @Transactional
    public AccountResponseDTO patchAccount(Long id, AccountUpdateDTO data) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        if (data.name() != null)
            account.setName(data.name().trim());
        if (data.type() != null)
            account.setType(data.type());
        if (data.balance() != null) {
            if (data.balance().compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException("Saldo não pode ser negativo");
            }
            account.setBalance(data.balance());
        }

        account = accountRepository.save(account);
        return new AccountResponseDTO(
                account.getId(),
                account.getUser().getId(),
                account.getName(),
                account.getType(),
                account.getBalance(),
                account.getCreatedAt());
    }

    // deleta contas
    @Transactional
    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new AccountNotFoundException(id);
        }
        accountRepository.deleteById(id);
    }

}
