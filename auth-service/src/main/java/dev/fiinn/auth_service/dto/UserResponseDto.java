package dev.fiinn.auth_service.dto;

import dev.fiinn.auth_service.model.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UserResponseDto extends BaseResponseDto{
    private UUID id;
    private String email;
    private Set<Role> roles;
    private boolean active;
}
