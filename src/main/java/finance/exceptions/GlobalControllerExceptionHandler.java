package finance.exceptions;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

/**
 * Global Exception Handler para tratar todas as exceções da aplicação
 * Fornece respostas de erro padronizadas e detalhadas para diferentes tipos de exceções
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    /**
     * Handler para UserNotFoundException - Usuário não encontrado
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(
            UserNotFoundException ex, WebRequest request) {

        logger.warn("Usuário não encontrado: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
            .message(ex.getMessage())
            .path(extractPath(request))
            .errorCode("USER_NOT_FOUND")
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handler para AccountNotFoundException - Conta não encontrada
     */
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFoundException(
            AccountNotFoundException ex, WebRequest request) {

        logger.warn("Conta não encontrada: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
            .message(ex.getMessage())
            .path(extractPath(request))
            .errorCode("ACCOUNT_NOT_FOUND")
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handler para ResourceNotFoundException - Recurso não encontrado
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        logger.warn("Recurso não encontrado: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
            .message(ex.getMessage())
            .path(extractPath(request))
            .errorCode("RESOURCE_NOT_FOUND")
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handler para DuplicateResourceException - Recurso duplicado
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
            DuplicateResourceException ex, WebRequest request) {

        logger.warn("Recurso duplicado: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.CONFLICT.value())
            .error(HttpStatus.CONFLICT.getReasonPhrase())
            .message(ex.getMessage())
            .path(extractPath(request))
            .errorCode("DUPLICATE_RESOURCE")
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Handler para InsufficientBalanceException - Saldo insuficiente
     */
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientBalanceException(
            InsufficientBalanceException ex, WebRequest request) {

        logger.warn("Saldo insuficiente: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .message(ex.getMessage())
            .path(extractPath(request))
            .errorCode("INSUFFICIENT_BALANCE")
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler para UnauthorizedAccessException - Acesso não autorizado
     */
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccessException(
            UnauthorizedAccessException ex, WebRequest request) {

        logger.warn("Acesso não autorizado: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.FORBIDDEN.value())
            .error(HttpStatus.FORBIDDEN.getReasonPhrase())
            .message(ex.getMessage())
            .path(extractPath(request))
            .errorCode("UNAUTHORIZED_ACCESS")
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Handler para BusinessException - Exceções de regra de negócio
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, WebRequest request) {

        logger.warn("Erro de negócio: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .message(ex.getMessage())
            .path(extractPath(request))
            .errorCode("BUSINESS_RULE_VIOLATION")
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler para validação de dados (@Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        logger.warn("Erro de validação em: {}", extractPath(request));

        List<ValidationFieldError> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            Object rejectedValue = ((FieldError) error).getRejectedValue();
            errors.add(new ValidationFieldError(fieldName, errorMessage, rejectedValue));
        });

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            "Erro de validação nos dados enviados",
            extractPath(request),
            "VALIDATION_ERROR",
            errors
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler para 404 - Endpoint não encontrado
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
            NoHandlerFoundException ex, WebRequest request) {

        logger.warn("Endpoint não encontrado: {}", ex.getRequestURL());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
            .message("Endpoint não encontrado: " + ex.getRequestURL())
            .path(extractPath(request))
            .errorCode("ENDPOINT_NOT_FOUND")
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handler para TransactionNotFoundException - Transação não encontrada
     */
    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTransactionNotFoundException(
            TransactionNotFoundException ex, WebRequest request) {

        logger.warn("Transação não encontrada: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
            .message(ex.getMessage())
            .path(extractPath(request))
            .errorCode("TRANSACTION_NOT_FOUND")
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handler para AuthenticationException customizada - Erro de autenticação
     */
    @ExceptionHandler(finance.exceptions.AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleCustomAuthenticationException(
            finance.exceptions.AuthenticationException ex, WebRequest request) {

        logger.warn("Erro de autenticação: {} - Código: {}", ex.getMessage(), ex.getErrorCode());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.UNAUTHORIZED.value())
            .error("Unauthorized")
            .message(ex.getMessage())
            .path(extractPath(request))
            .errorCode(ex.getErrorCode())
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handler para AuthenticationException do Spring Security
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleSpringAuthenticationException(
            AuthenticationException ex, WebRequest request) {

        logger.warn("Falha na autenticação Spring Security: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.UNAUTHORIZED.value())
            .error("Unauthorized")
            .message("Credenciais inválidas ou token expirado")
            .path(extractPath(request))
            .errorCode("AUTHENTICATION_FAILED")
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handler para AccessDeniedException - Acesso negado
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {

        logger.warn("Acesso negado: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.FORBIDDEN.value())
            .error(HttpStatus.FORBIDDEN.getReasonPhrase())
            .message("Você não tem permissão para acessar este recurso")
            .path(extractPath(request))
            .errorCode("ACCESS_DENIED")
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Handler para HttpMessageNotReadableException - Corpo da requisição inválido
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, WebRequest request) {

        logger.error("Erro ao ler corpo da requisição: {}", ex.getMessage());

        String message = "Formato do corpo da requisição inválido";
        if (ex.getMessage().contains("JSON")) {
            message = "JSON inválido. Verifique a sintaxe do corpo da requisição";
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .message(message)
            .path(extractPath(request))
            .errorCode("MALFORMED_JSON")
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler para HttpRequestMethodNotSupportedException - Método HTTP não suportado
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, WebRequest request) {

        logger.warn("Método HTTP não suportado: {}", ex.getMethod());

        String supportedMethods = ex.getSupportedHttpMethods() != null
            ? String.join(", ", ex.getSupportedHttpMethods().toString())
            : "Nenhum";

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.METHOD_NOT_ALLOWED.value())
            .error(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase())
            .message(String.format("Método %s não é suportado para esta requisição. Métodos suportados: %s",
                    ex.getMethod(), supportedMethods))
            .path(extractPath(request))
            .errorCode("METHOD_NOT_ALLOWED")
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Handler para HttpMediaTypeNotSupportedException - Tipo de mídia não suportado
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, WebRequest request) {

        logger.warn("Tipo de mídia não suportado: {}", ex.getContentType());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
            .error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase())
            .message(String.format("Tipo de mídia '%s' não é suportado. Use: application/json",
                    ex.getContentType()))
            .path(extractPath(request))
            .errorCode("UNSUPPORTED_MEDIA_TYPE")
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * Handler para MethodArgumentTypeMismatchException - Tipo de argumento inválido
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {

        logger.warn("Tipo de argumento inválido: {} - esperado: {}",
                ex.getName(), ex.getRequiredType());

        String message = String.format("Parâmetro '%s' deve ser do tipo %s",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconhecido");

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .message(message)
            .path(extractPath(request))
            .errorCode("INVALID_PARAMETER_TYPE")
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler para MissingServletRequestParameterException - Parâmetro obrigatório ausente
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, WebRequest request) {

        logger.warn("Parâmetro obrigatório ausente: {}", ex.getParameterName());

        String message = String.format("Parâmetro obrigatório '%s' está ausente",
                ex.getParameterName());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .message(message)
            .path(extractPath(request))
            .errorCode("MISSING_PARAMETER")
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler para ConstraintViolationException - Violação de constraint
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {

        logger.warn("Violação de constraint: {}", ex.getMessage());

        List<ValidationFieldError> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            Object invalidValue = violation.getInvalidValue();
            errors.add(new ValidationFieldError(fieldName, message, invalidValue));
        }

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            "Erro de validação de dados",
            extractPath(request),
            "CONSTRAINT_VIOLATION",
            errors
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler para DataIntegrityViolationException - Violação de integridade do banco
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, WebRequest request) {

        logger.error("Violação de integridade do banco de dados", ex);

        String message = "Erro de integridade dos dados";
        String errorCode = "DATA_INTEGRITY_VIOLATION";

        // Detectar tipos específicos de erro
        if (ex.getMessage().contains("Duplicate entry") || ex.getMessage().contains("duplicate key")) {
            message = "Este registro já existe no sistema";
            errorCode = "DUPLICATE_ENTRY";
        } else if (ex.getMessage().contains("foreign key constraint")) {
            message = "Não é possível realizar esta operação devido a dependências existentes";
            errorCode = "FOREIGN_KEY_VIOLATION";
        } else if (ex.getMessage().contains("cannot be null")) {
            message = "Campo obrigatório está ausente";
            errorCode = "NULL_VALUE_NOT_ALLOWED";
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.CONFLICT.value())
            .error(HttpStatus.CONFLICT.getReasonPhrase())
            .message(message)
            .path(extractPath(request))
            .errorCode(errorCode)
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Handler genérico para todas as outras exceções não tratadas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {

        // Gerar ID único para rastreamento
        String traceId = UUID.randomUUID().toString();

        // Log do erro completo para debug
        logger.error("Erro interno no servidor [TraceID: {}]: {}", traceId, ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
            .message("Ocorreu um erro interno no servidor. Por favor, contate o suporte informando o código de rastreamento")
            .path(extractPath(request))
            .errorCode("INTERNAL_SERVER_ERROR")
            .traceId(traceId)
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Método auxiliar para extrair o caminho da requisição
     */
    private String extractPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }

    /**
     * Classe interna para resposta de erro padrão
     */
    public static class ErrorResponse {
        private final LocalDateTime timestamp;
        private final int status;
        private final String error;
        private final String message;
        private final String path;
        private final String errorCode;
        private final String traceId;

        private ErrorResponse(Builder builder) {
            this.timestamp = builder.timestamp;
            this.status = builder.status;
            this.error = builder.error;
            this.message = builder.message;
            this.path = builder.path;
            this.errorCode = builder.errorCode;
            this.traceId = builder.traceId;
        }

        public static Builder builder() {
            return new Builder();
        }

        // Getters
        public LocalDateTime getTimestamp() { return timestamp; }
        public int getStatus() { return status; }
        public String getError() { return error; }
        public String getMessage() { return message; }
        public String getPath() { return path; }
        public String getErrorCode() { return errorCode; }
        public String getTraceId() { return traceId; }

        /**
         * Builder para ErrorResponse
         */
        public static class Builder {
            private LocalDateTime timestamp = LocalDateTime.now();
            private int status;
            private String error;
            private String message;
            private String path;
            private String errorCode;
            private String traceId;

            public Builder timestamp(LocalDateTime timestamp) {
                this.timestamp = timestamp;
                return this;
            }

            public Builder status(int status) {
                this.status = status;
                return this;
            }

            public Builder error(String error) {
                this.error = error;
                return this;
            }

            public Builder message(String message) {
                this.message = message;
                return this;
            }

            public Builder path(String path) {
                this.path = path;
                return this;
            }

            public Builder errorCode(String errorCode) {
                this.errorCode = errorCode;
                return this;
            }

            public Builder traceId(String traceId) {
                this.traceId = traceId;
                return this;
            }

            public ErrorResponse build() {
                return new ErrorResponse(this);
            }
        }
    }

    /**
     * Classe para resposta de erro de validação
     */
    public static class ValidationErrorResponse {
        private final LocalDateTime timestamp;
        private final int status;
        private final String error;
        private final String message;
        private final String path;
        private final String errorCode;
        private final List<ValidationFieldError> validationErrors;

        public ValidationErrorResponse(LocalDateTime timestamp, int status, String error,
                                      String message, String path, String errorCode,
                                      List<ValidationFieldError> validationErrors) {
            this.timestamp = timestamp;
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;
            this.errorCode = errorCode;
            this.validationErrors = validationErrors;
        }

        // Getters
        public LocalDateTime getTimestamp() { return timestamp; }
        public int getStatus() { return status; }
        public String getError() { return error; }
        public String getMessage() { return message; }
        public String getPath() { return path; }
        public String getErrorCode() { return errorCode; }
        public List<ValidationFieldError> getValidationErrors() { return validationErrors; }
    }

    /**
     * Classe para representar um erro de validação em um campo específico
     */
    public static class ValidationFieldError {
        private final String field;
        private final String message;
        private final Object rejectedValue;

        public ValidationFieldError(String field, String message, Object rejectedValue) {
            this.field = field;
            this.message = message;
            this.rejectedValue = rejectedValue;
        }

        // Getters
        public String getField() { return field; }
        public String getMessage() { return message; }
        public Object getRejectedValue() { return rejectedValue; }
    }
}