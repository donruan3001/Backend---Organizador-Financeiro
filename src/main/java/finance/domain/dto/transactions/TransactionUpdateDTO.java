package finance.domain.dto.transactions;

import finance.domain.transactions.CategoryTransactions;
import finance.domain.transactions.TypeTransaction;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionUpdateDTO(
        String name,
        CategoryTransactions category,
        TypeTransaction type,
        @Positive(message = "O valor deve ser positivo")
        BigDecimal amount
) {
}
