package dev.fiinn.profile_service.dto;

import dev.fiinn.profile_service.enums.ProfileStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponseDto {

    private String profileId;

    private UUID userId;

    private ProfileStatus status;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private LocalDateTime updatedAt;

    private LocalDateTime statusLastUpdated;
}
