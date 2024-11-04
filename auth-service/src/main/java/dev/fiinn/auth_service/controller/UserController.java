package dev.fiinn.auth_service.controller;

import dev.fiinn.auth_service.dto.*;
import dev.fiinn.auth_service.exception.UserNotFoundException;
import dev.fiinn.auth_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto dto) {
        return ResponseEntity.ok(
                DtoConverter.toUserResponse(userService.registerUser(dto))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @Valid @RequestBody UserUpdateDto dto) {
        return ResponseEntity.ok(
                DtoConverter.toUserResponse(userService.updateUser(id, dto))
        );
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable UUID id, @Valid @RequestBody PasswordChangeDto dto) {
        return ResponseEntity.ok(
                BaseResponseDto.builder()
                        .timestamp(LocalDateTime.now())
                        .message("Password changed successfully")
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(
                DtoConverter.toUserResponse(userService.getUserById(id)
                        .orElseThrow(UserNotFoundException::new))
        );
    }
}
