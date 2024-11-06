package dev.fiinn.auth_service;

import dev.fiinn.auth_service.dto.AuthResponseDto;
import dev.fiinn.auth_service.dto.UserLoginDto;
import dev.fiinn.auth_service.exception.InvalidCredentialsException;
import dev.fiinn.auth_service.model.User;
import dev.fiinn.auth_service.security.AuthService;
import dev.fiinn.auth_service.security.JwtUtil;
import dev.fiinn.auth_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private UserLoginDto loginDto;
    private User user;

    @BeforeEach
    void setUp() {
        loginDto = new UserLoginDto();
        loginDto.setEmail("test@ex.com");
        loginDto.setPassword("Password123$");

        user = new User();
        user.setEmail("test@ex.com");
        user.setPassword("encodedPassword");
        user.setActive(true);
    }

    @Test
    void whenLoginWithValidCredentials_thenSucceed() {

        when(userService.getUserByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(user.getEmail())).thenReturn("jwt-token");

        AuthResponseDto response = authService.login(loginDto);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("jwt-token");
        assertThat(response.getUser().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void whenLoginWithInvalidPassword_thenThrowException() {
        when(userService.getUserByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.login(loginDto));
    }
}
