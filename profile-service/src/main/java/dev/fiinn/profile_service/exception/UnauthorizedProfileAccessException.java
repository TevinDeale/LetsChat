package dev.fiinn.profile_service.exception;

public class UnauthorizedProfileAccessException extends RuntimeException{
    public UnauthorizedProfileAccessException(String message){
        super(message);
    }
}
