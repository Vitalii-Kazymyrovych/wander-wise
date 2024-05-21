package wander.wise.application.dto.exception;

public record ExceptionResponseDto(
        String timestamp,
        ExceptionDto exception,
        ExceptionDto cause) {
    public ExceptionResponseDto setCause(ExceptionDto cause) {
        return new ExceptionResponseDto(timestamp, exception, cause);
    }
}
