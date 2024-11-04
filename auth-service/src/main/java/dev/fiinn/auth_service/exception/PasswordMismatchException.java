package dev.fiinn.auth_service.exception;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException() {
        super("New and Confirm password does not match");
    }
}
