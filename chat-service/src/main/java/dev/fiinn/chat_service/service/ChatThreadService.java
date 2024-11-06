package dev.fiinn.chat_service.service;

import dev.fiinn.chat_service.dto.ChatThreadCreationDto;
import dev.fiinn.chat_service.enums.Role;
import dev.fiinn.chat_service.model.ChatThread;

import java.util.List;
import java.util.UUID;

public interface ChatThreadService {
    ChatThread createChatThread(ChatThreadCreationDto dto);
    String generateThreadInviteCode();
    boolean removeUserFromThread(String threadId, UUID moderatorId, UUID userToRemoveId);
    boolean updateRolesAsOwner(String threadId, UUID ownerId, UUID userId, Role role);
    boolean updateThreadStatus(String threadId, Boolean status);
    boolean deleteThread(String threadId, UUID creatorId);
    boolean leaveThread(String threadId, UUID userId);
    ChatThread joinThreadWithInviteCode(String inviteCode, UUID userId);
    ChatThread getThreadById(String threadId);
    List<ChatThread> getThreadsForUser(UUID userId);
    ChatThread getThreadByInviteCode(String inviteCode);
    boolean isUserInThread(String threadId, UUID userId);
}
