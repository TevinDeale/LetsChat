package dev.fiinn.chat_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import java.util.*;

@Data
@Builder
public class ChatThreadCreationDto {

    @NotBlank(message = "Thread name is required")
    @Size(max = 100, message = "Thread name must be less than 100 characters")
    private String threadName;

    @NotNull(message = "Creator ID is required")
    private UUID creatorId;

    private String description;

    @Builder.Default
    private Integer maxParticipants = 50;
}
