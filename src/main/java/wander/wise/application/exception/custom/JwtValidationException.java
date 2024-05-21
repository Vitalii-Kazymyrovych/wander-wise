package wander.wise.application.exception.custom;

public class JwtValidationException extends RuntimeException {
    public JwtValidationException(String message, Exception exception) {
        super(message, exception);
    }
}
