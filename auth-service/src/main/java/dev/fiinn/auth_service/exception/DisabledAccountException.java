package dev.fiinn.auth_service.exception;

public class DisabledAccountException extends RuntimeException {
    public DisabledAccountException() {
        super("Account is disabled");
    }
}
