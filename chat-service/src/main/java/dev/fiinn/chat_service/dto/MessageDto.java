package dev.fiinn.chat_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MessageDto {

    @NotNull(message = "ThreadId cannot be blank")
    private String threadId;

    @NotNull(message = "userID cannot be blank")
    private UUID userId;

    @NotNull(message = "Message contents cannot be blank")
    @Size(min = 1, max = 500, message = "Message content has to be between 1 and 500 characters")
    String content;
}
