package dev.fiinn.chat_service.dto;

import java.time.LocalDateTime;

public class DtoConverter {
    public static ErrorResponseDto toErrorResponse(String message, String error, String path, Integer status) {
        return ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .error(error)
                .path(path)
                .status(status)
                .build();
    }
}
