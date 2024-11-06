package dev.fiinn.chat_service.exception;

public class ThreadNotFoundException extends RuntimeException{
    public ThreadNotFoundException(String message) {
        super(message);
    }
}
