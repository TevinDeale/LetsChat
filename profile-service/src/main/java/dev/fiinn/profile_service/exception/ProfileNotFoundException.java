package dev.fiinn.profile_service.exception;

public class ProfileNotFoundException extends RuntimeException{
    public ProfileNotFoundException(String message){
        super(message);
    }
}