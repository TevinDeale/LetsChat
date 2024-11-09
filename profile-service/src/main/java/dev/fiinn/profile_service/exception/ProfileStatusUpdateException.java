package dev.fiinn.profile_service.exception;

public class ProfileStatusUpdateException extends RuntimeException{
    public ProfileStatusUpdateException(String message){
        super(message);
    }
}
