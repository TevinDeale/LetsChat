package dev.fiinn.chat_service;

import dev.fiinn.chat_service.dto.MessageDto;
import dev.fiinn.chat_service.exception.MessageErrorException;
import dev.fiinn.chat_service.exception.RequiredPermissionsException;
import dev.fiinn.chat_service.model.ChatThread;
import dev.fiinn.chat_service.model.Message;
import dev.fiinn.chat_service.service.ChatThreadService;
import dev.fiinn.chat_service.service.MessageService;
import dev.fiinn.chat_service.websocket.ChatWebSocketController;
import dev.fiinn.chat_service.websocket.MessageType;
import dev.fiinn.chat_service.websocket.WebSocketMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatWebSocketControllerTest {
    @Mock
    private MessageService messageService;

    @Mock
    private ChatThreadService chatThreadService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ChatWebSocketController webSocketController;

    private WebSocketMessage testWebSocketMessage;
    private MessageDto testMessageDto;
    private Message testMessage;
    private UUID testUserId;
    private String testThreadId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testThreadId = "test-thread-id";

        testWebSocketMessage = WebSocketMessage.builder()
                .threadId(testThreadId)
                .sender(testUserId)
                .content("Test message")
                .messageType(MessageType.CHAT)
                .timestamp(LocalDateTime.now())
                .build();

        testMessage = Message.builder()
                .messageId("test-message-id")
                .threadId(testThreadId)
                .userId(testUserId)
                .message("Test message")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Test
    void handleChatMessage_Success() {
        // Arrange
        when(chatThreadService.isUserInThread(testThreadId, testUserId)).thenReturn(true);
        when(messageService.sendMessage(ArgumentMatchers.any(MessageDto.class))).thenReturn(testMessage);
        doNothing().when(messagingTemplate).convertAndSend(anyString(), any(Message.class));

        // Act
        webSocketController.handleChatMessage(testThreadId, testWebSocketMessage);

        // Assert
        verify(messageService).sendMessage(any(MessageDto.class));
        verify(messagingTemplate).convertAndSend("/chat/thread/" + testThreadId, testMessage);
    }

    @Test
    void handleChatMessage_UserNotInThread() {

        when(chatThreadService.isUserInThread(testThreadId, testUserId)).thenReturn(false);
        doNothing().when(messagingTemplate).convertAndSendToUser(
                anyString(),
                anyString(),
                any(MessageErrorException.class)
        );

        webSocketController.handleChatMessage(testThreadId, testWebSocketMessage);

        verify(messageService, never()).sendMessage(any(MessageDto.class));
    }

    @Test
    void handleJoin_Success() {

        ChatThread testThread = ChatThread.builder()
                .threadName("Test Thread")
                .creatorId(UUID.randomUUID())
                .build();

        when(chatThreadService.getThreadById(testThreadId)).thenReturn(testThread);
        doNothing().when(messagingTemplate).convertAndSend(anyString(), any(WebSocketMessage.class));

        webSocketController.handleJoin(testThreadId, testWebSocketMessage);

        verify(messagingTemplate).convertAndSend(
                eq("/chat/thread/" + testThreadId),
                any(WebSocketMessage.class)
        );
    }

    @Test
    void handleLeave_Success() {

        ChatThread testThread = ChatThread.builder()
                .threadName("Test Thread")
                .creatorId(UUID.randomUUID())
                .build();

        when(chatThreadService.getThreadById(testThreadId)).thenReturn(testThread);
        doNothing().when(messagingTemplate).convertAndSend(anyString(), any(WebSocketMessage.class));

        webSocketController.handleLeave(testThreadId, testWebSocketMessage);

        verify(messagingTemplate).convertAndSend(
                eq("/chat/thread/" + testThreadId),
                any(WebSocketMessage.class)
        );
    }
}
