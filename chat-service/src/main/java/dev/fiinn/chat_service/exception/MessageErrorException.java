package dev.fiinn.chat_service.exception;

public class MessageErrorException extends RuntimeException {
    public MessageErrorException(String message) {
        super(message);
    }
}
