package dev.fiinn.auth_service.exception;

public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException() {
        super("Credentials are invalid");
    }
}
