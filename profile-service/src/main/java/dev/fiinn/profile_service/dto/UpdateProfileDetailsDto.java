package dev.fiinn.profile_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileDetailsDto {

    @Size(min = 5, max = 15, message = "Username has to be between 5 and 15 characters.")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]+$", message = "Username must start with a letter and contain only letters and numbers")
    private String username;

    @NotBlank(message = "profileId cannot be empty")
    private String profileId;

    @Size(min = 1, max = 15)
    @Pattern(regexp = "^[a-zA-Z\\s-]+$", message = "Name must contain only letters, spaces, and hyphens")
    private String firstName;

    @Size(min = 1, max = 15)
    @Pattern(regexp = "^[a-zA-Z\\s-]+$", message = "Name must contain only letters, spaces, and hyphens")
    private String lastName;

    @Email
    private String email;
}
