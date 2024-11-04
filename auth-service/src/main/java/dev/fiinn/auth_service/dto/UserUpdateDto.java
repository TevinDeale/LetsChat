package dev.fiinn.auth_service.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserUpdateDto {
    @Email(message = "Must be a valid email address")
    private String email;
    
    private Boolean active;
}
