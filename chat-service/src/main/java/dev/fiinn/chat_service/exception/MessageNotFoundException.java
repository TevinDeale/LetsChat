package dev.fiinn.chat_service.exception;

public class MessageNotFoundException extends  RuntimeException{
    public MessageNotFoundException(String message) {
        super(message);
    }
}
