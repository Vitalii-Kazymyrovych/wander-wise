package wander.wise.application.exception.custom;

public class EmailServiceException extends RuntimeException {
    public EmailServiceException(String message, Exception exception) {
        super(message, exception);
    }
}
