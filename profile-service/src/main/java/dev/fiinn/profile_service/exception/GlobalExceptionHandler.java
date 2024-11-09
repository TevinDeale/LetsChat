package dev.fiinn.profile_service.exception;

import dev.fiinn.profile_service.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    ResponseEntity<ErrorResponse> handleDuplicateResourceException(
            Exception err,
            WebRequest request) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(toErrorResponse(
                        err.getClass().getName(),
                        err.getMessage(),
                        request,
                        HttpStatus.BAD_REQUEST.value()
                ));
    }

    @ExceptionHandler(InvalidProfileDataException.class)
    ResponseEntity<ErrorResponse> handleInvalidProfileDataException(
            Exception err,
            WebRequest request) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(toErrorResponse(
                        err.getClass().getName(),
                        err.getMessage(),
                        request,
                        HttpStatus.BAD_REQUEST.value()
                ));
    }

    @ExceptionHandler(ProfileDeletionException.class)
    ResponseEntity<ErrorResponse> handleProfileDeletionException(
            Exception err,
            WebRequest request) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(toErrorResponse(
                        err.getClass().getName(),
                        err.getMessage(),
                        request,
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                ));
    }

    @ExceptionHandler(ProfileException.class)
    ResponseEntity<ErrorResponse> handleProfileException(
            Exception err,
            WebRequest request) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(toErrorResponse(
                        err.getClass().getName(),
                        err.getMessage(),
                        request,
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                ));
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    ResponseEntity<ErrorResponse> handleProfileNotFoundException(
            Exception err,
            WebRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(toErrorResponse(
                        err.getClass().getName(),
                        err.getMessage(),
                        request,
                        HttpStatus.NOT_FOUND.value()
                ));
    }

    @ExceptionHandler(ProfileStatusUpdateException.class)
    ResponseEntity<ErrorResponse> handleProfileStatusUpdateException(
            Exception err,
            WebRequest request) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(toErrorResponse(
                        err.getClass().getName(),
                        err.getMessage(),
                        request,
                        HttpStatus.BAD_REQUEST.value()
                ));
    }

    @ExceptionHandler(UnauthorizedProfileAccessException.class)
    ResponseEntity<ErrorResponse> handleUnauthorizedProfileAccessException(
            Exception err,
            WebRequest request) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(toErrorResponse(
                        err.getClass().getName(),
                        err.getMessage(),
                        request,
                        HttpStatus.FORBIDDEN.value()
                ));
    }

    private String getPath(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }

    private ErrorResponse toErrorResponse(String message, String errorMessage, WebRequest request, int status) {
        return ErrorResponse.builder()
                .errorMessage(message)
                .error(errorMessage)
                .path(getPath(request))
                .httpStatus(status)
                .build();
    }
}
