package dev.fiinn.chat_service.model;

import dev.fiinn.chat_service.enums.MessageStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "messages")
public class Message {

    @MongoId
    private String messageId;

    @NotNull(message = "UserId cannot be blank")
    private UUID userId;

    @NotNull(message = "Message cannot be blank")
    @Size(min = 1, max = 500, message = "Message must be between 1 and 500 characters")
    private String message;

    @NotNull(message = "ThreadId cannot be blank")
    @Indexed
    private String threadId;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    @NotNull(message = "Message status cannot be blank")
    private MessageStatus messageStatus;

    @Builder.Default
    private Set<UUID> readBy = new HashSet<>();

    @Builder.Default
    private Set<UUID> deliveredTo = new HashSet<>();

    private LocalDateTime readAt;
}
