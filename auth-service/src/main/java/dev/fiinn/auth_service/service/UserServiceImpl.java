package dev.fiinn.auth_service.service;

import dev.fiinn.auth_service.dto.PasswordChangeDto;
import dev.fiinn.auth_service.dto.UserRegistrationDto;
import dev.fiinn.auth_service.dto.UserUpdateDto;
import dev.fiinn.auth_service.exception.EmailAlreadyExistException;
import dev.fiinn.auth_service.exception.InvalidPasswordException;
import dev.fiinn.auth_service.exception.PasswordMismatchException;
import dev.fiinn.auth_service.exception.UserNotFoundException;
import dev.fiinn.auth_service.model.Role;
import dev.fiinn.auth_service.model.User;
import dev.fiinn.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.procedure.ParameterStrategyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistException();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(List.of(Role.USER)));

        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        Optional<User> foundUser = userRepository.findById(id);

        if (foundUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        return foundUser;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        Optional<User> foundUser = userRepository.findByEmail(email);

        if (foundUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        return foundUser;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new UserNotFoundException();
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException();
        }

        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void changePassword(UUID userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidPasswordException("Invalid old password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void toggleUserActive(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        user.setActive(!user.isActive());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void addRole(UUID id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeRole(UUID id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        user.getRoles().remove(role);
        userRepository.save(user);
    }

    @Override
    public User registerUser(UserRegistrationDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistException();
        }

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .roles(new HashSet<>(List.of(Role.USER)))
                .active(true)
                .build();

        return userRepository.save(user);
    }

    @Override
    public User updateUser(UUID id, UserUpdateDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        if (!dto.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistException();
        }

        user.setEmail(dto.getEmail());

        if (dto.getActive() != null) {
            user.setActive(dto.getActive());
        }

        return userRepository.save(user);
    }

    @Override
    public void changePassword(UUID id, PasswordChangeDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(user.getPassword(), dto.getCurrentPassword())) {
            throw new InvalidPasswordException("Current password is invalid");
        }

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordMismatchException();
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }
}
