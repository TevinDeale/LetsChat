package dev.fiinn.chat_service.repository;

import dev.fiinn.chat_service.enums.MessageStatus;
import dev.fiinn.chat_service.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findAllByThreadId(String threadId);
    List<Message> findByTimestampAfter(LocalDateTime timestamp);
    Optional<Message> findFirstByThreadIdOrderByTimestampDesc(String threadId);
    List<Message> findByMessageStatus(MessageStatus status);
    List<Message> findAllByUserIdAndMessageStatusAndThreadId(UUID userId, MessageStatus status, String threadId);
}
