package wander.wise.application.dto.exception;

public record ExceptionDto(String name, String message, StackTraceElement occurredIn) {
}
