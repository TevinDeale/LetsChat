package dev.fiinn.profile_service.exception;

public class InvalidProfileDataException extends  RuntimeException{
    public InvalidProfileDataException(String message) {
        super(message);
    }
}
