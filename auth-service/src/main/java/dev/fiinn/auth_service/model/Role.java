package dev.fiinn.auth_service.model;

public enum Role {
    OWNER("Full control over chat thread"),
    MODERATOR("Can manage messages and users"),
    USER("Basic chat privileges");

    private final String description;

    Role(String description) {
        this.description = description;
    }
}
