package finance.exceptions;

public class AccountNotFoundException extends ResourceNotFoundException {

    public AccountNotFoundException(Long accountId) {
        super("Conta", accountId);
    }

    public AccountNotFoundException(String message) {
        super(message);
    }
}
