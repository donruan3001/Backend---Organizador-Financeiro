package finance.services;


import java.util.List;

import org.springframework.stereotype.Service;

import finance.config.AuthenticatedUser;
import finance.domain.acounts.Account;
import finance.domain.dto.accounts.AccountCreateDTO;
import finance.domain.dto.accounts.AccountResponseDTO;
import finance.domain.dto.accounts.AccountUpdateDTO;
import finance.domain.user.User;
import finance.exceptions.AccountNotFoundException;
import finance.exceptions.BusinessException;
import finance.exceptions.UnauthorizedAccessException;
import finance.exceptions.UserNotFoundException;
import finance.repository.RepositoryAccount;
import finance.repository.RepositoryUser;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;


@Service
public class ServiceAccount {

    private final  RepositoryAccount accountRepository;
    private final  RepositoryUser userRepository;

    public ServiceAccount( RepositoryAccount accountRepository, RepositoryUser userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;}
    
    @Transactional
    public AccountResponseDTO createAccount(AccountCreateDTO data) {

      Long userAuthLong = AuthenticatedUser.getAuthenticatedUserId();

      User user = userRepository.findById(userAuthLong).orElseThrow(()-> new UserNotFoundException(userAuthLong));

        Account account = new Account(data.name().trim(), data.type(), data.balance());
        
        account.setUser(user);

        accountRepository.save(account);

        return AccountResponseDTO.toDTO(account);
    }

    public List<AccountResponseDTO> getAllAccountsUser() {

      Long userAuthLong = AuthenticatedUser.getAuthenticatedUserId();

      userRepository.findById(userAuthLong).orElseThrow(()-> new UserNotFoundException(userAuthLong));

      List<Account> accounts=accountRepository.findByUserId(userAuthLong);
        return accounts.stream().map(AccountResponseDTO::toDTO).toList();
    }

    public AccountResponseDTO getAccountById(Long id) {
        Long userId = AuthenticatedUser.getAuthenticatedUserId();
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        // Verify ownership
        if (!account.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("Conta", id);
        }

        return AccountResponseDTO.toDTO(account);
    }

    @Transactional
    public AccountResponseDTO updateAccount(Long id, AccountUpdateDTO data) {
        Long userId = AuthenticatedUser.getAuthenticatedUserId();
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        if (!account.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("Conta", id);
        }

        // Update fields if provided
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

        accountRepository.save(account);
        return AccountResponseDTO.toDTO(account);
    }

    @Transactional
    public void deleteAccount(Long id) {
        Long userId = AuthenticatedUser.getAuthenticatedUserId();
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        if (!account.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("Conta", id);
        }

        accountRepository.delete(account);
    }

}
