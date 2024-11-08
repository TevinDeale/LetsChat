package dev.fiinn.chat_service.exception;

import dev.fiinn.chat_service.dto.DtoConverter;
import dev.fiinn.chat_service.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ThreadNotFoundException.class)
    ResponseEntity<ErrorResponseDto> handleThreadNotFoundException(
            Exception err,
            WebRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(DtoConverter.toErrorResponse(
                        "Thread not found",
                        err.getMessage(),
                        getPath(request),
                        HttpStatus.NOT_FOUND.value()
                ));
    }

    @ExceptionHandler(MessageNotFoundException.class)
    ResponseEntity<ErrorResponseDto> handleMessageNotFoundException(
            Exception err,
            WebRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(DtoConverter.toErrorResponse(
                        "Message not found",
                        err.getMessage(),
                        getPath(request),
                        HttpStatus.NOT_FOUND.value()
                ));
    }

    @ExceptionHandler(RequiredPermissionsException.class)
    ResponseEntity<ErrorResponseDto> handleRequiredPermissionsException(
            Exception err,
            WebRequest request) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(DtoConverter.toErrorResponse(
                        "This account does not have permission to perform this operation",
                        err.getMessage(),
                        getPath(request),
                        HttpStatus.FORBIDDEN.value()
                ));
    }

    @ExceptionHandler(InvalidInviteCodeException.class)
    ResponseEntity<ErrorResponseDto> handleInvalidInviteCodeException(
            Exception err,
            WebRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(DtoConverter.toErrorResponse(
                        "Invalid invite code",
                        err.getMessage(),
                        getPath(request),
                        HttpStatus.NOT_FOUND.value()
                ));
    }

    @ExceptionHandler(ParticipantNotFoundException.class)
    ResponseEntity<ErrorResponseDto> handleParticipantNotFoundException(
            Exception err,
            WebRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(DtoConverter.toErrorResponse(
                        "Participant not found",
                        err.getMessage(),
                        getPath(request),
                        HttpStatus.NOT_FOUND.value()
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(
            Exception err,
            WebRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(DtoConverter.toErrorResponse(
                        "Participant not found",
                        err.getMessage(),
                        getPath(request),
                        HttpStatus.NOT_ACCEPTABLE.value()
                ));
    }

    @ExceptionHandler(ChatThreadErrorException.class)
    ResponseEntity<ErrorResponseDto> handleChatThreadErrorException(
            Exception err,
            WebRequest request) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(DtoConverter.toErrorResponse(
                        "Unexpected error occurred",
                        err.getMessage(),
                        getPath(request),
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                ));
    }

    @ExceptionHandler(MessageErrorException.class)
    ResponseEntity<ErrorResponseDto> handleMessageErrorException(
            Exception err,
            WebRequest request) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(DtoConverter.toErrorResponse(
                        "Error Processing message",
                        err.getMessage(),
                        getPath(request),
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                ));
    }

    private String getPath(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }
}
