package dev.fiinn.auth_service.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class TokenBlacklistService {
    private static final Logger logger = LoggerFactory.getLogger(TokenBlacklistService.class);

    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();
    private final JwtUtil jwtUtil;

    public TokenBlacklistService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void cleanupBlacklist() {
        logger.info("Starting token blacklist cleanup");
        int initialSize = blacklistedTokens.size();

        Set<String> expiredTokens = blacklistedTokens.stream()
                .filter(token -> {
                    try {
                        return jwtUtil.isTokenExpired(token);
                    } catch (Exception e) {
                        logger.warn("Error checking token expiration: {}", e.getMessage());
                        return true;
                    }
                })
                .collect(Collectors.toSet());

        blacklistedTokens.removeAll(expiredTokens);

        logger.info("Blacklist cleanup completed. Removed {} expired tokens. Current size: {}",
                initialSize - blacklistedTokens.size(),
                blacklistedTokens.size());
    }
}
