package finance.services;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import finance.domain.acounts.Account;
import finance.domain.acounts.AccountType;
import finance.domain.dto.accounts.AccountCreateDTO;

public class ServiceAccountTest {

        private final  ServiceAccount serviceAccount;
        
        public ServiceAccountTest(ServiceAccount serviceAccount){
            this.serviceAccount=serviceAccount;
        }

    @Test
    void testCreateAccount() {

        Account account = new Account("nome",AccountType.CONTA_CONJUNTA, BigDecimal.ONE);

        AccountCreateDTO accountCreateDTO= new AccountCreateDTO(account);
    
      serviceAccount.createAccount(accountCreateDTO);

      Assertions.assertEquals(account, accountCreateDTO);
    }

    @Test
    void testDeleteAccount() {

    }

    @Test
    void testGetAccountById() {

    }

    @Test
    void testGetAllAccountsUser() {

    }

    @Test
    void testUpdateAccount() {

    }
}
