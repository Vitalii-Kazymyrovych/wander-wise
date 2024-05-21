package wander.wise.application.exception.custom;

public class MapsServiceException extends RuntimeException {
    public MapsServiceException(String message, Exception cause) {
        super(message, cause);
    }

    public MapsServiceException(String message) {
        super(message);
    }
}
