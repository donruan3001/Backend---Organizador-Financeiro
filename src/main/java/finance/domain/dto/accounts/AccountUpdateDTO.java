package finance.domain.dto.accounts;

import finance.domain.acounts.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record AccountUpdateDTO(
                @Size(min = 1, max = 120, message = "Nome deve ter entre 1 e 120 caracteres")
                String name,

                AccountType type,

                @DecimalMin(value = "0.0", message = "Saldo não pode ser negativo")
                BigDecimal balance) {
}
