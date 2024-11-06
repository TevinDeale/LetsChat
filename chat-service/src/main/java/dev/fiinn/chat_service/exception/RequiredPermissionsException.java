package dev.fiinn.chat_service.exception;

public class RequiredPermissionsException extends RuntimeException {
    public RequiredPermissionsException(String message) {
        super(message);
    }
}
