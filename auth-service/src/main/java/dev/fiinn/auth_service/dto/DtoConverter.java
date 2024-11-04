package dev.fiinn.auth_service.dto;

import dev.fiinn.auth_service.model.User;

import java.time.LocalDateTime;

public class DtoConverter {
    public static UserResponseDto toUserResponse(User user) {
        return UserResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .id(user.getId())
                .email(user.getEmail())
                .roles(user.getRoles())
                .active(user.isActive())
                .build();
    }

    public static AuthResponseDto toAuthResponse(String token, User user) {
        return AuthResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .message("Authentication Successful")
                .token(token)
                .user(toUserResponse(user))
                .build();
    }

    public static ErrorResponseDto toErrorResponse(String message, String error, String path, int status) {
        return ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .error(error)
                .path(path)
                .status(status)
                .build();
    }
}
