package ru.slisarenko.spring_security_jwt.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.slisarenko.spring_security_jwt.model.RefreshToken;
import ru.slisarenko.spring_security_jwt.repository.RefreshTokenRepository;

@Slf4j
@Service
@Transactional
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

    public void revokeRefreshToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshToken -> {
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
            log.info("Refresh token revoked: {}", token);
        });
    }

    @Transactional(readOnly = true)
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            log.warn("Refresh token expired for user: {}", token.getUser().getUsername());
            throw new BadCredentialsException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }
}
