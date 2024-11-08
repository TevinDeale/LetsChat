package dev.fiinn.chat_service.websocket;

import dev.fiinn.chat_service.model.UserSessionInfo;
import dev.fiinn.chat_service.service.ChatThreadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventHandler {
    private final ChatThreadService chatThreadService;
    private final SimpMessageSendingOperations messagingTemplate;

    private final Map<String, UserSessionInfo> activeSessions = new ConcurrentHashMap<>();

    @EventListener
    public void handleWebSocketConnect(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        UUID userId = extractUserId(headerAccessor);
        String threadId = extractThreadId(headerAccessor);

        activeSessions.put(sessionId, new UserSessionInfo(userId, threadId));

        log.debug("User connected - sessionID: {}, userId: {}, threadId: {}",
                sessionId, userId, threadId);
    }

    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        activeSessions.remove(sessionId);

        log.debug("User disconnected - sessionId: {}", sessionId);
    }

    private UUID extractUserId(StompHeaderAccessor headerAccessor) {
        return UUID.fromString(Objects.requireNonNull(headerAccessor.getFirstNativeHeader("userId")));
    }

    private String extractThreadId(StompHeaderAccessor headerAccessor) {
        return headerAccessor.getFirstNativeHeader("threadId");
    }
}
