package dev.fiinn.chat_service.repository;

import dev.fiinn.chat_service.enums.Role;
import dev.fiinn.chat_service.model.ChatThread;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatThreadRepository extends MongoRepository<ChatThread, String> {
    Optional<ChatThread> findByInviteCode(String code);
    List<ChatThread> findByParticipantsContaining(UUID participantId);
    List<ChatThread> findChatThreadsByActive(Boolean isActive);
    List<ChatThread> findChatThreadsByCreatorId(UUID creatorId);

    @Query("{'participantRoles.?0': ?1}")
    List<ChatThread> findByUserRole(UUID id, Role role);
}
