package dev.fiinn.chat_service.model;

import dev.fiinn.chat_service.enums.Role;
import dev.fiinn.chat_service.exception.ParticipantNotFoundException;
import dev.fiinn.chat_service.exception.RequiredPermissionsException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_threads")
public class ChatThread {

    @MongoId
    private String id;

    @NotBlank(message = "Thread name is required")
    @Size(max = 100, message = "Thread name must be less than 100 characters")
    private String threadName;

    @NotNull(message = "Creator ID is required")
    private UUID creatorId;

    @Indexed(unique = true)
    @NotBlank(message = "Invite code is required")
    private String inviteCode;

    private LocalDateTime createTimestamp;
    private LocalDateTime lastMessageTimestamp;

    @Builder.Default
    private Set<UUID> participants = new HashSet<>();

    @Builder.Default
    private Map<UUID, Role> participantRoles = new HashMap<>();

    private String description;

    @Builder.Default
    private Integer maxParticipants = 100;

    @Builder.Default
    private Boolean active = true;

    public void addParticipant(UUID id, Role role) {
        if (isParticipant(id)) {
            throw new IllegalArgumentException("Participant with id: " + id + " is already apart of this thread");
        }

        participants.add(id);
        participantRoles.put(id, role);
    }

    public void removeParticipant(UUID id) {
        if (!isParticipant(id)) {
            throw new ParticipantNotFoundException("Participant with id: " + id + " was not found in this thread");
        }

        participantRoles.remove(id);
        participants.remove(id);
    }

    public void updateParticipantRole(UUID roleToUpdate, UUID whoRequested, Role role) {

        if (!hasPermission(whoRequested, role)) {
            throw new RequiredPermissionsException(
                    "User: " + id + " does not have permission to complete this request."
            );
        }

        Role currentRole = getParticipantRole(roleToUpdate);

        if (currentRole.equals(role)) {
            throw new IllegalArgumentException("User already has requested role");
        }

        participantRoles.put(roleToUpdate, role);
    }

    public int participantSize() {
        return participants.size();
    }

    public boolean isParticipant(UUID id) {
        return participants.contains(id);
    }

    public Role getParticipantRole(UUID id) {
        if (!isParticipant(id)) {
            throw new ParticipantNotFoundException("Participant with id: " + id + " was not found in this thread");
        }

        return participantRoles.get(id);
    }

    public boolean hasPermission(UUID id, Role requestedRole) {

        Role userRole = getParticipantRole(id);

        if (requestedRole.equals(Role.OWNER) && !userRole.equals(Role.OWNER)) {
            throw new RequiredPermissionsException(
                    "User: " + id + " does not have permission to make this user a owner of the thread. Only owners can make other participants owners."
            );
        }

        return !userRole.equals(Role.USER);
    }
}
