package finance.domain.dto.accounts;

import java.math.BigDecimal;

import finance.domain.acounts.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccountCreateDTO(

                @NotBlank(message = "account.name.required") String name,

                @NotNull(message = "account.type.required") AccountType type,

                @NotNull(message = "account.balance.required") @DecimalMin(value = "0.00", inclusive = true, message = "account.balance.negative") BigDecimal balance

) {

    
}
