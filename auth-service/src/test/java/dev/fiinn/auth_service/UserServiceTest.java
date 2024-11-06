package dev.fiinn.auth_service;

import dev.fiinn.auth_service.dto.UserRegistrationDto;
import dev.fiinn.auth_service.exception.EmailAlreadyExistException;
import dev.fiinn.auth_service.model.Role;
import dev.fiinn.auth_service.model.User;
import dev.fiinn.auth_service.repository.UserRepository;
import dev.fiinn.auth_service.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegistrationDto registrationDto;
    private User user;

    @BeforeEach
    void setUp() {
        registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("test@ex.com");
        registrationDto.setPassword("Password123$");

        user = new User();
        user.setEmail("test@ex.com");
        user.setPassword("encodedPassword");
        user.setRoles(Set.of(Role.USER));
        user.setActive(true);
    }

    @Test
    void whenRegisterUser_thenSucceed() {
        when(userRepository.existsByEmail(registrationDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registrationDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.registerUser(registrationDto);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(registrationDto.getEmail());
        assertThat(result.getRoles().contains(Role.USER)).isTrue();
        assertThat(result.isActive()).isTrue();
    }

    @Test
    void whenRegisterUserWithExistingEmail_thenThrowException() {
        when(userRepository.existsByEmail(registrationDto.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistException.class, () -> userService.registerUser(registrationDto));
    }

    @Test
    void whenGetUserByEmail_thenReturnUser() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByEmail(user.getEmail());

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(user.getEmail());
    }
}
