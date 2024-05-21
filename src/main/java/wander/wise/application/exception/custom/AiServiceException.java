package wander.wise.application.exception.custom;

public class AiServiceException extends RuntimeException {
    public AiServiceException(String message, Exception exception) {
        super(message, exception);
    }
}
