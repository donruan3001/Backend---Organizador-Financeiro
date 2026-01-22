package finance.exceptions;

import java.math.BigDecimal;

public class InsufficientBalanceException extends BusinessException {

    public InsufficientBalanceException(BigDecimal currentBalance, BigDecimal requiredAmount) {
        super(String.format("Saldo insuficiente. Saldo atual: R$ %.2f, Valor necessário: R$ %.2f",
                currentBalance, requiredAmount));
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
