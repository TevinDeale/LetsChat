package dev.fiinn.auth_service.exception;

public class EmailAlreadyExistException extends RuntimeException{
    public EmailAlreadyExistException() {
        super("Email already exist");
    }
}
