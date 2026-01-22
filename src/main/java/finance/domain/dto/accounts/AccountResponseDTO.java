// finance/api/dto/AccountResponseDTO.java
package finance.domain.dto.accounts;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import finance.domain.acounts.Account;
import finance.domain.acounts.AccountType;

public record AccountResponseDTO(
                Long id,
                Long userId,
                String name,
                AccountType type,
                BigDecimal balance,
                LocalDateTime createdAt) {

        public static AccountResponseDTO toDTO(Account a) {
                return new AccountResponseDTO(
                                a.getId(),
                                a.getUser().getId(),
                                a.getName(),
                                a.getType(),
                                a.getBalance(),
                                a.getCreatedAt());
        }

}
