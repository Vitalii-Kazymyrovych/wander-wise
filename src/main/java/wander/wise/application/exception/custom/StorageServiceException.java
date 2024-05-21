package wander.wise.application.exception.custom;

public class StorageServiceException extends RuntimeException {
    public StorageServiceException(String message, Exception exception) {
        super(message, exception);
    }
}
