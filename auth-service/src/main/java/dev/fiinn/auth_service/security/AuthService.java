package dev.fiinn.auth_service.security;

import dev.fiinn.auth_service.dto.AuthResponseDto;
import dev.fiinn.auth_service.dto.DtoConverter;
import dev.fiinn.auth_service.dto.UserLoginDto;
import dev.fiinn.auth_service.exception.DisabledAccountException;
import dev.fiinn.auth_service.exception.InvalidCredentialsException;
import dev.fiinn.auth_service.model.User;
import dev.fiinn.auth_service.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserService userService,
                       PasswordEncoder encoder,
                       JwtUtil jwtUtil) {
        this.userService = userService;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponseDto login(UserLoginDto loginDto) {
        User user = userService.getUserByEmail(loginDto.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!user.isActive()) {
            throw new DisabledAccountException();
        }

        if (!encoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return AuthResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .message("Login successful")
                .token(token)
                .user(DtoConverter.toUserResponse(user))
                .build();
    }
}
