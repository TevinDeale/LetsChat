package dev.fiinn.auth_service.controller;

import dev.fiinn.auth_service.dto.AuthResponseDto;
import dev.fiinn.auth_service.dto.BaseResponseDto;
import dev.fiinn.auth_service.dto.UserLoginDto;
import dev.fiinn.auth_service.security.AuthService;
import dev.fiinn.auth_service.security.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthController(AuthService authService, TokenBlacklistService tokenBlacklistService) {
        this.authService = authService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody UserLoginDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null) {
            tokenBlacklistService.blacklistToken(token);
            return ResponseEntity.ok()
                    .body(BaseResponseDto.builder()
                            .timestamp(LocalDateTime.now())
                            .message("Logged out successfully")
                            .build());
        }
        return ResponseEntity.badRequest()
                .body(BaseResponseDto.builder()
                        .timestamp(LocalDateTime.now())
                        .message("No token provided")
                        .build());
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
