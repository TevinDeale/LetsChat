package dev.fiinn.chat_service.websocket;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class WebSocketMessage {
    private String content;
    private UUID sender;
    private MessageType messageType;
    private LocalDateTime timestamp;
    private String threadId;
}
