package dev.fiinn.auth_service.service;

import dev.fiinn.auth_service.dto.PasswordChangeDto;
import dev.fiinn.auth_service.dto.UserRegistrationDto;
import dev.fiinn.auth_service.dto.UserUpdateDto;
import dev.fiinn.auth_service.model.Role;
import dev.fiinn.auth_service.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    // CRUD Operations
    User createUser(User user);
    Optional<User> getUserById(UUID id);
    Optional<User> getUserByEmail(String email);
    List<User> getAllUsers();
    User updateUser(User user);
    void deleteUser(UUID id);

    // Authentication
    boolean existsByEmail(String email);
    void changePassword(UUID userId, String oldPassword, String newPassword);
    void toggleUserActive(UUID id);

    // Role Management
    void addRole(UUID id, Role role);
    void removeRole(UUID id, Role role);

    // Account Management
    User registerUser(UserRegistrationDto registrationDto);
    User updateUser(UUID id, UserUpdateDto updateDto);
    void changePassword(UUID id, PasswordChangeDto passwordChangeDto);
}
