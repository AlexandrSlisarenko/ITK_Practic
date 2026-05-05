package ru.slisarenko.spring_security_jwt.service;

import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.slisarenko.spring_security_jwt.model.RefreshToken;
import ru.slisarenko.spring_security_jwt.repository.RefreshTokenRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserDAOService userDAOService;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenDurationMs;

    public RefreshToken createRefreshToken(String username) {
        var user = userDAOService.loadUserByUsername(username);

        refreshTokenRepository.findByUser(user).ifPresent(refreshTokenRepository::delete);

        var refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .isRevoked(false)
                .build();

        refreshToken = refreshTokenRepository.save(refreshToken);
        log.info("Refresh token created for user: {}", username);

        return refreshToken;
    }
}
