package dev.fiinn.chat_service;

import dev.fiinn.chat_service.dto.ChatThreadCreationDto;
import dev.fiinn.chat_service.enums.Role;
import dev.fiinn.chat_service.model.ChatThread;
import dev.fiinn.chat_service.repository.ChatThreadRepository;
import dev.fiinn.chat_service.service.ChatThreadServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ChatThreadServiceTest {
    @Mock
    private ChatThreadRepository threadRepository;

    @InjectMocks
    private ChatThreadServiceImpl chatThreadService;

    private ChatThread testThread;
    private ChatThreadCreationDto testDto;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();

        testDto = ChatThreadCreationDto.builder()
                .threadName("Test Thread")
                .creatorId(testUserId)
                .maxParticipants(50)
                .description("Test Description")
                .build();

        testThread = ChatThread.builder()
                .threadName("Test Thread")
                .creatorId(testUserId)
                .maxParticipants(50)
                .description("Test Description")
                .active(true)
                .createTimestamp(LocalDateTime.now())
                .inviteCode("12345678")
                .build();

        testThread.addParticipant(testUserId, Role.OWNER);
    }

    @Test
    void createChatThread_Success() {

        when(threadRepository.save(Mockito.<ChatThread>any(ChatThread.class))).thenReturn(testThread);

        ChatThread result = chatThreadService.createChatThread(testDto);

        assertNotNull(result);
        assertEquals(testDto.getThreadName(), result.getThreadName());
        assertEquals(testDto.getCreatorId(), result.getCreatorId());
        assertTrue(result.getActive());
    }

    @Test
    void removeUserFromThread_AsOwner_Success() {

        UUID userToRemove = UUID.randomUUID();
        testThread.addParticipant(userToRemove, Role.USER);
        when(threadRepository.findById(anyString())).thenReturn(Optional.of(testThread));

        assertDoesNotThrow(() ->
                chatThreadService.removeUserFromThread("123", testUserId, userToRemove)
        );
    }
}
