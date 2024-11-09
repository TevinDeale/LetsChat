package dev.fiinn.profile_service.exception;

public class ProfileDeletionException extends RuntimeException{
    public ProfileDeletionException(String message){
        super(message);
    }
}
