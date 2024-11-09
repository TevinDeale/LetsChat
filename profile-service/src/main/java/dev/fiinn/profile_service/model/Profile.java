package dev.fiinn.profile_service.model;

import dev.fiinn.profile_service.enums.ProfileStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.annotation.Collation;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document(collection = "user_profiles")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    @MongoId
    private String profileId;

    @NotNull(message = "UserId cannot be null")
    @Indexed
    private UUID userId;

    @NotNull(message = "Profile Status cannot be null")
    @Builder.Default
    private ProfileStatus status = ProfileStatus.OFFLINE;

    @NotNull(message = "First name cannot be null")
    @Size(min = 1, max = 15)
    @Pattern(regexp = "^[a-zA-Z\\s-]+$", message = "Name must contain only letters, spaces, and hyphens")
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @Size(min = 1, max = 15)
    @Pattern(regexp = "^[a-zA-Z\\s-]+$", message = "Name must contain only letters, spaces, and hyphens")
    private String lastName;

    @NotNull(message = "Username cannot be null")
    @Size(min = 5, max = 15, message = "Username has to be between 5 and 15 characters.")
    @Indexed(unique = true)
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]+$", message = "Username must start with a letter and contain only letters and numbers")
    private String username;

    @Email
    @Indexed(unique = true)
    private String email;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime statusLastUpdated = LocalDateTime.now();

    public void updateStatus(ProfileStatus updatedStatus) {
        status = updatedStatus;
        statusLastUpdated = LocalDateTime.now();
    }
}
