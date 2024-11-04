package dev.fiinn.auth_service.exception;

import dev.fiinn.auth_service.dto.DtoConverter;
import dev.fiinn.auth_service.dto.ErrorResponseDto;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(
            Exception err,
            WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(DtoConverter.toErrorResponse(
                        "An unexpected error occurred",
                        err.getMessage(),
                        getPath(request),
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                ));
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ErrorResponseDto> handleEmailAlreadyExistException(
            EmailAlreadyExistException err, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(DtoConverter.toErrorResponse(
                        "Email Already exist",
                        err.getMessage(),
                        getPath(request),
                        HttpStatus.BAD_REQUEST.value()
                ));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(
            UserNotFoundException err, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(DtoConverter.toErrorResponse(
                        "User not found",
                        err.getMessage(),
                        getPath(request),
                        HttpStatus.NOT_FOUND.value()
                ));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidPasswordException(
            InvalidPasswordException err, WebRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(DtoConverter.toErrorResponse(
                        "Password is Invalid",
                        err.getMessage(),
                        getPath(request),
                        HttpStatus.UNAUTHORIZED.value()
                ));
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handlePasswordMismatchException(
            PasswordMismatchException err, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(DtoConverter.toErrorResponse(
                        "Passwords do not match",
                        err.getMessage(),
                        getPath(request),
                        HttpStatus.BAD_REQUEST.value()
                ));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidCredentialException(
            InvalidCredentialsException err, WebRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(DtoConverter.toErrorResponse(
                        "Invalid Credentials",
                        err.getMessage(),
                        getPath(request),
                        HttpStatus.UNAUTHORIZED.value()
                ));
    }

    @ExceptionHandler(DisabledAccountException.class)
    public ResponseEntity<ErrorResponseDto> handleDisabledAccountException(
            DisabledAccountException err, WebRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(DtoConverter.toErrorResponse(
                        "Account is disabled",
                        err.getMessage(),
                        getPath(request),
                        HttpStatus.FORBIDDEN.value()
                ));
    }

    private String getPath(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }
}
