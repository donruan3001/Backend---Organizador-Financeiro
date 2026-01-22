package finance.exceptions;

public class UnauthorizedAccessException extends BusinessException {

    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public UnauthorizedAccessException(String resourceName, Long resourceId) {
        super(String.format("Você não tem permissão para acessar %s com ID %d", resourceName, resourceId));
    }
}
