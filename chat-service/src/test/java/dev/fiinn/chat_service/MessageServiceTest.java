package dev.fiinn.chat_service;

import dev.fiinn.chat_service.dto.ChatThreadCreationDto;
import dev.fiinn.chat_service.dto.MessageDto;
import dev.fiinn.chat_service.enums.Role;
import dev.fiinn.chat_service.exception.ParticipantNotFoundException;
import dev.fiinn.chat_service.exception.RequiredPermissionsException;
import dev.fiinn.chat_service.model.ChatThread;
import dev.fiinn.chat_service.model.Message;
import dev.fiinn.chat_service.queue.ChatMessageProducer;
import dev.fiinn.chat_service.repository.ChatThreadRepository;
import dev.fiinn.chat_service.repository.MessageRepository;
import dev.fiinn.chat_service.service.ChatThreadService;
import dev.fiinn.chat_service.service.ChatThreadServiceImpl;
import dev.fiinn.chat_service.service.MessageServiceImpl;
import org.hibernate.validator.constraints.ModCheck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {
    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ChatThreadService chatThreadService;

    @Mock
    private ChatMessageProducer chatMessageProducer;

    @InjectMocks
    private MessageServiceImpl messageService;

    private Message testMessage;
    private MessageDto testMessageDto;
    private UUID testUserId;
    private String testThreadId;
    private ChatThread testThread;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testThreadId = "test-thread-id";

        testMessageDto = MessageDto.builder()
                .threadId(testThreadId)
                .userId(testUserId)
                .content("Test message")
                .build();

        testMessage = Message.builder()
                .messageId("test-message-id")
                .threadId(testThreadId)
                .userId(testUserId)
                .message("Test message")
                .timestamp(LocalDateTime.now())
                .build();

        testThread = ChatThread.builder()
                .threadName("Test Thread")
                .creatorId(testUserId)
                .build();

        testThread.addParticipant(testUserId, Role.OWNER);
    }

    @Test
    void sendMessage_Success() {

        when(chatThreadService.isUserInThread(testThreadId, testUserId)).thenReturn(true);
        when(chatThreadService.getThreadById(testThreadId)).thenReturn(testThread);
        when(messageRepository.save(Mockito.<Message>any())).thenReturn(testMessage);

        Message result = messageService.sendMessage(testMessageDto);

        assertNotNull(result);
        assertEquals(testMessageDto.getContent(), result.getMessage());
        assertEquals(testMessageDto.getThreadId(), result.getThreadId());
        assertEquals(testMessageDto.getUserId(), result.getUserId());
        verify(messageRepository).save(Mockito.<Message>any(Message.class));
    }

    @Test
    void sendMessage_UserNotInThread_ThrowsException() {

        when(chatThreadService.isUserInThread(testThreadId, testUserId)).thenReturn(false);

        assertThrows(ParticipantNotFoundException.class, () ->
                messageService.sendMessage(testMessageDto)
        );
    }

    @Test
    void getMessages_Success() {

        List<Message> messages = Collections.singletonList(testMessage);
        when(messageRepository.findAllByThreadId(testThreadId)).thenReturn(messages);

        List<Message> result = messageService.getMessages(testThreadId);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(testMessage.getMessage(), result.getFirst().getMessage());
    }

    @Test
    void markMessageAsRead_Success() {

        when(messageRepository.findById(testMessage.getMessageId()))
                .thenReturn(Optional.of(testMessage));
        when(messageRepository.save(Mockito.<Message>any())).thenReturn(testMessage);

        boolean result = messageService.markMessageAsRead(testMessage.getMessageId(), testUserId);

        assertTrue(result);
        verify(messageRepository).save(Mockito.<Message>any(Message.class));
    }

    @Test
    void deleteMessage_AsMessageOwner_Success() {

        when(messageRepository.findById(testMessage.getMessageId()))
                .thenReturn(Optional.of(testMessage));
        when(chatThreadService.getThreadById(testThreadId)).thenReturn(testThread);

        boolean result = messageService.deleteMessage(testMessage.getMessageId(), testUserId);

        assertTrue(result);
        verify(messageRepository).delete(testMessage);
    }

    @Test
    void deleteMessage_NotOwner_ThrowsException() {

        UUID differentUserId = UUID.randomUUID();
        testThread.addParticipant(differentUserId, Role.USER);
        when(messageRepository.findById(testMessage.getMessageId()))
                .thenReturn(Optional.of(testMessage));
        when(chatThreadService.getThreadById(testThreadId)).thenReturn(testThread);

        assertThrows(RequiredPermissionsException.class, () ->
                messageService.deleteMessage(testMessage.getMessageId(), differentUserId)
        );
    }
}
