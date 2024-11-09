package dev.fiinn.profile_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ErrorResponse {

    private LocalDateTime timestamp = LocalDateTime.now();
    private String error;
    private String errorMessage;
    private int httpStatus;
    private String path;
}
