package finance.exceptions;

/**
 * Exceção lançada quando uma transação não é encontrada
 */
public class TransactionNotFoundException extends ResourceNotFoundException {

    public TransactionNotFoundException(Long transactionId) {
        super("Transação", transactionId);
    }

    public TransactionNotFoundException(String message) {
        super(message);
    }
}
