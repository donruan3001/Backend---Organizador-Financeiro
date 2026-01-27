package finance.exceptions;

/**
 * Exceção lançada quando há falha na autenticação do usuário
 * Deve ser usada em casos de credenciais inválidas, token expirado, etc.
 */
public class AuthenticationException extends RuntimeException {

    private final String errorCode;

    public AuthenticationException(String message) {
        super(message);
        this.errorCode = "AUTH_ERROR";
    }

    public AuthenticationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "AUTH_ERROR";
    }

    public String getErrorCode() {
        return errorCode;
    }

    // Métodos estáticos para criar exceções com contexto específico
    public static AuthenticationException invalidCredentials() {
        return new AuthenticationException("Email ou senha inválidos", "INVALID_CREDENTIALS");
    }

    public static AuthenticationException tokenExpired() {
        return new AuthenticationException("Token de autenticação expirado", "TOKEN_EXPIRED");
    }

    public static AuthenticationException tokenInvalid() {
        return new AuthenticationException("Token de autenticação inválido", "TOKEN_INVALID");
    }

    public static AuthenticationException userNotAuthenticated() {
        return new AuthenticationException("Usuário não autenticado", "NOT_AUTHENTICATED");
    }

    public static AuthenticationException accountLocked() {
        return new AuthenticationException("Conta bloqueada. Contate o suporte", "ACCOUNT_LOCKED");
    }

    public static AuthenticationException accountDisabled() {
        return new AuthenticationException("Conta desabilitada", "ACCOUNT_DISABLED");
    }
}
