package wander.wise.application.exception.custom;

public class ImageSearchServiceException extends RuntimeException {
    public ImageSearchServiceException(String message, Exception exception) {
        super(message, exception);
    }
}
