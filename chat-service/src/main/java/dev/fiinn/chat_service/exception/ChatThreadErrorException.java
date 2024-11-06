package dev.fiinn.chat_service.exception;

public class ChatThreadErrorException extends RuntimeException{
    public ChatThreadErrorException(String message) {
        super(message);
    }
}
