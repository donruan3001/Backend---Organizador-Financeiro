package finance.exceptions;

public class DuplicateResourceException extends BusinessException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String resourceName, String identifier) {
        super(String.format("%s '%s' já está cadastrado", resourceName, identifier));
    }
}
