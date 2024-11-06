package dev.fiinn.chat_service.service;

import dev.fiinn.chat_service.dto.ChatThreadCreationDto;
import dev.fiinn.chat_service.enums.Role;
import dev.fiinn.chat_service.exception.ChatThreadErrorException;
import dev.fiinn.chat_service.exception.RequiredPermissionsException;
import dev.fiinn.chat_service.exception.ThreadNotFoundException;
import dev.fiinn.chat_service.model.ChatThread;
import dev.fiinn.chat_service.repository.ChatThreadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
public class ChatThreadServiceImpl implements ChatThreadService{

    private final ChatThreadRepository threadRepository;
    private final Role owner = Role.OWNER;
    private final Role mod = Role.MODERATOR;
    private final Role user = Role.USER;

    public ChatThreadServiceImpl(ChatThreadRepository threadRepository) {
        this.threadRepository = threadRepository;
    }

    @Override
    public ChatThread createChatThread(ChatThreadCreationDto dto) {
        Objects.requireNonNull(dto, "ChatThreadCreationDto cannot be null");
        ChatThread newChatThread = ChatThread.builder()
                .threadName(dto.getThreadName())
                .description(dto.getDescription())
                .creatorId(dto.getCreatorId())
                .maxParticipants(dto.getMaxParticipants())
                .inviteCode(generateThreadInviteCode())
                .createTimestamp(LocalDateTime.now())
                .active(true)
                .build();

        newChatThread.addParticipant(dto.getCreatorId(), owner);
        return threadRepository.save(newChatThread);
    }

    @Override
    public String generateThreadInviteCode() {
        String inviteCode;
        int attempts = 0;
        final int MAX_ATTEMPTS = 5;

        do {
            inviteCode = generateRandomString();
            attempts++;

            if (attempts >= MAX_ATTEMPTS) {
                throw new ChatThreadErrorException("There was error generating invite code");
            }
        } while (threadRepository.findByInviteCode(inviteCode).isPresent());

        return inviteCode;
    }

    @Override
    public boolean removeUserFromThread(String threadId, UUID moderatorId, UUID userToRemoveId) {
        ChatThread chatThread = findThreadById(threadId);

        Role modRole = chatThread.getParticipantRole(moderatorId);
        Role userRole = chatThread.getParticipantRole(userToRemoveId);

        if (modRole.equals(user)) {
            throw new RequiredPermissionsException("A user cannot delete other users in a thread");
        }

        if (userRole.equals(owner) && !moderatorId.equals(chatThread.getCreatorId())) {
            throw new RequiredPermissionsException("A owner can only be removed by the creator of the thread");
        }

        if (userRole.equals(mod) && !modRole.equals(owner)) {
            throw new RequiredPermissionsException("A moderator can only be removed by an owner of the thread");
        }

        chatThread.removeParticipant(userToRemoveId);
        return true;
    }

    @Override
    public boolean updateRolesAsOwner(String threadId, UUID ownerId, UUID userId, Role role) {
        ChatThread chatThread = findThreadById(threadId);

        if (!chatThread.getParticipantRole(ownerId).equals(owner)) {
            throw new RequiredPermissionsException("Only an owner of the thread update roles for users");
        }

        if(!chatThread.getParticipantRole(userId).equals(role)) {
            throw new IllegalArgumentException("User already has the requested role: " + role);
        }

        chatThread.updateParticipantRole(userId, ownerId, role);
        return true;
    }

    @Override
    public boolean updateThreadStatus(String threadId, Boolean status) {
        ChatThread chatThread = findThreadById(threadId);

        chatThread.setActive(status);
        threadRepository.save(chatThread);

        return true;
    }

    @Override
    public boolean deleteThread(String threadId, UUID creatorId) {
        ChatThread chatThread = findThreadById(threadId);

        if (!chatThread.getCreatorId().equals(creatorId)) {
            throw new RequiredPermissionsException("Only the creator of the thread can delete the thread");
        }

        threadRepository.delete(chatThread);
        return true;
    }

    @Override
    public boolean leaveThread(String threadId, UUID userId) {
        ChatThread chatThread = findThreadById(threadId);

        if (chatThread.getCreatorId().equals(userId)) {
            throw new IllegalArgumentException("As the owner you will need to delete the thread to leave the thread");
        }

        chatThread.removeParticipant(userId);
        return true;
    }

    @Override
    public ChatThread joinThreadWithInviteCode(String inviteCode, UUID userId) {
        ChatThread chatThread = getThreadByInviteCode(inviteCode);

        if (chatThread.participantSize() >= chatThread.getMaxParticipants()) {
            throw new ChatThreadErrorException("Thread has reached maximum participants");
        }

        chatThread.addParticipant(userId, user);
        return threadRepository.save(chatThread);
    }

    @Override
    public ChatThread getThreadById(String threadId) {
        return findThreadById(threadId);
    }

    @Override
    public List<ChatThread> getThreadsForUser(UUID userId) {
        return threadRepository.findByParticipantsContaining(userId);
    }

    @Override
    public ChatThread getThreadByInviteCode(String inviteCode) {
        return threadRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new ThreadNotFoundException("Thread with invite code: " + inviteCode + " was not found"));
    }

    @Override
    public boolean isUserInThread(String threadId, UUID userId) {
        ChatThread chatThread = findThreadById(threadId);
        return chatThread.isParticipant(userId);
    }

    private ChatThread findThreadById(String threadId) {

        return threadRepository.findById(threadId)
                .orElseThrow(() -> new ThreadNotFoundException(
                        "Thread with ID: " + threadId + " was not found"
                ));
    }

    private String generateRandomString() {
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numeric = "1234567890";

        String alphaNumeric = alpha + numeric;

        StringBuilder randString = new StringBuilder(8);

        for (int x = 0; x < 8; x++) {
            int randNum = (int) (alphaNumeric.length() * Math.random());

            randString.append(alphaNumeric.charAt(randNum));
        }

        return randString.toString();
    }
}
