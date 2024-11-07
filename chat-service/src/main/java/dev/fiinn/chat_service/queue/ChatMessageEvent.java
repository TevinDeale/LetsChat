package dev.fiinn.chat_service.queue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageEvent {
    private String messageId;
    private String threadId;
    private UUID senderId;
    private String content;
    private LocalDateTime timestamp;
}
