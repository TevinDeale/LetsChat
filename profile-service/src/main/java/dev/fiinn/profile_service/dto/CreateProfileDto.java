package dev.fiinn.profile_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProfileDto {

    @NotNull(message = "UserId cannot be null")
    private UUID userId;

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
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]+$", message = "Username must start with a letter and contain only letters and numbers")
    private String username;

    @Email
    private String email;
}
