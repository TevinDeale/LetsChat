package dev.fiinn.chat_service.exception;

public class ParticipantNotFoundException extends RuntimeException{
    public ParticipantNotFoundException(String message) {
        super(message);
    }
}
