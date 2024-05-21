package wander.wise.application.exception;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import wander.wise.application.dto.exception.ExceptionDto;
import wander.wise.application.dto.exception.ExceptionResponseDto;
import wander.wise.application.exception.custom.AuthorizationException;
import wander.wise.application.exception.custom.CardSearchException;
import wander.wise.application.exception.custom.EmailServiceException;
import wander.wise.application.exception.custom.ImageSearchServiceException;
import wander.wise.application.exception.custom.JwtValidationException;
import wander.wise.application.exception.custom.AiServiceException;
import wander.wise.application.exception.custom.MapsServiceException;
import wander.wise.application.exception.custom.RegistrationException;
import wander.wise.application.exception.custom.StorageServiceException;

import static wander.wise.application.constants.GlobalConstants.TIMESTAMP_FORMAT;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    public static final int EXCEPTION_LOCATION_INDEX = 0;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST);
        List<String> errors = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(this::getErrorMessage)
                .toList();
        body.put("errors", errors);
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ExceptionResponseDto> handleException(Throwable exception) {
        return new ResponseEntity<>(getResponseMessage(exception), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleEntityNotFoundException(EntityNotFoundException exception) {
        return new ResponseEntity<>(getResponseMessage(exception), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AiServiceException.class)
    public ResponseEntity<ExceptionResponseDto> handleAiException(AiServiceException exception) {
        return new ResponseEntity<>(getResponseMessage(exception), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ExceptionResponseDto> handleAuthorizationException(AuthorizationException exception) {
        return new ResponseEntity<>(getResponseMessage(exception), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CardSearchException.class)
    public ResponseEntity<ExceptionResponseDto> handleCardSearchException(CardSearchException exception) {
        return new ResponseEntity<>(getResponseMessage(exception), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailServiceException.class)
    public ResponseEntity<ExceptionResponseDto> handleEmailServiceException(EmailServiceException exception) {
        return new ResponseEntity<>(getResponseMessage(exception), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ImageSearchServiceException.class)
    public ResponseEntity<ExceptionResponseDto> handleImageSearchException(ImageSearchServiceException exception) {
        return new ResponseEntity<>(getResponseMessage(exception), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JwtValidationException.class)
    public ResponseEntity<ExceptionResponseDto> handleJwtValidationException(JwtValidationException exception) {
        return new ResponseEntity<>(getResponseMessage(exception), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MapsServiceException.class)
    public ResponseEntity<ExceptionResponseDto> handleMapsException(MapsServiceException exception) {
        return new ResponseEntity<>(getResponseMessage(exception), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<ExceptionResponseDto> handleRegistrationException(RegistrationException exception) {
        return new ResponseEntity<>(getResponseMessage(exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ExceptionResponseDto> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception) {
        return new ResponseEntity<>(getResponseMessage(exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StorageServiceException.class)
    public ResponseEntity<ExceptionResponseDto> handleStorageServiceException(StorageServiceException exception) {
        return new ResponseEntity<>(getResponseMessage(exception), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getErrorMessage(ObjectError error) {
        if (error instanceof FieldError) {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            return field + ": " + message;
        }
        return error.getDefaultMessage();
    }

    private static ExceptionResponseDto getResponseMessage(Throwable exception) {
        String exceptionName = exception.getClass().getName();
        ExceptionDto exceptionDto
                = new ExceptionDto(
                        exceptionName.substring(exceptionName
                                .lastIndexOf(".") + 1),
                exception.getMessage(),
                exception.getStackTrace()[EXCEPTION_LOCATION_INDEX]);
        LocalDateTime timeStamp = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT);
        ExceptionResponseDto message = new ExceptionResponseDto(
                timeStamp.format(formatter),
                exceptionDto,
                null);
        if (exception.getCause() != null) {
            Throwable cause = exception.getCause();
            String causeName = cause.getClass().getName();
            ExceptionDto causeDto = new ExceptionDto(
                    causeName.substring(causeName
                            .lastIndexOf(".") + 1),
                    cause.getMessage(),
                    cause.getStackTrace()[EXCEPTION_LOCATION_INDEX]);
            message = message.setCause(causeDto);
        }
        return message;
    }
}
