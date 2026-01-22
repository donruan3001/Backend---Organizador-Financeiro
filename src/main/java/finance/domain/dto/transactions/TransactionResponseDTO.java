package finance.domain.dto.transactions;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import finance.domain.transactions.CategoryTransactions;

public record TransactionResponseDTO(
                Long transactionId,
                Long accountId,
                CategoryTransactions category,
                String name,
                BigDecimal amount,
                LocalDateTime created,
                LocalDateTime updated) {
        public TransactionResponseDTO(Long transactionId, Long accountId,
                        CategoryTransactions category, String name,
                        BigDecimal amount) {
                this(transactionId, accountId, category, name, amount, LocalDateTime.now(), LocalDateTime.now());
        }

}
