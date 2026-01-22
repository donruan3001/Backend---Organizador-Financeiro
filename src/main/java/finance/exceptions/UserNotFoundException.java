package finance.exceptions;

public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(String username) {
        super("Usuário", username);
    }

    public UserNotFoundException(Long userId) {
        super("Usuário com ID " + userId + " não encontrado");
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
