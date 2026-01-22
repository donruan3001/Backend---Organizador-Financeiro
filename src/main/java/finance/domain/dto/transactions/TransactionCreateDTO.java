package finance.domain.dto.transactions;

import finance.domain.transactions.CategoryTransactions;
import finance.domain.transactions.TypeTransaction;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionCreateDTO(
                @NotNull Long accountId,
                @NotNull String name,
                CategoryTransactions category,
                @NotNull TypeTransaction type,
                @NotNull @Positive BigDecimal amount) {
}
