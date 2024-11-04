package dev.fiinn.auth_service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AuthResponseDto extends BaseResponseDto{
    private String token;
    private UserResponseDto user;
}
