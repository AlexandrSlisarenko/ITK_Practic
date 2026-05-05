package ru.slisarenko.spring_security_jwt.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.slisarenko.spring_security_jwt.dto.AuthRequestDTO;
import ru.slisarenko.spring_security_jwt.dto.AuthResponseDTO;
import ru.slisarenko.spring_security_jwt.model.RefreshToken;
import ru.slisarenko.spring_security_jwt.model.Role;
import ru.slisarenko.spring_security_jwt.model.User;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserDAOService userDAOService;
    private final PasswordEncoder passwordEncoder;
    private final LoginService loginService;

    public AuthResponseDTO authenticate(AuthRequestDTO request) {
        log.info("Authentication attempt for user: {}", request.username());

        if (!userDAOService.isUserAccountNonLocked(request.username())) {
            log.warn("Authentication blocked for locked account: {}", request.username());
            throw new RuntimeException("Account is locked. Please contact administrator.");
        }

        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            loginService.loginSucceeded(request.username());

            var user = (User) authentication.getPrincipal();
            var accessToken = jwtService.generateAccessToken(user);
            var refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

            log.info("Successful authentication for user: {} with role: {}",
                    request.username(), user.getRole());

            return AuthResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getToken())
                    .tokenType("Bearer")
                    .expiresIn(jwtService.extractExpiration(accessToken).getTime())
                    .role(user.getRole().name())
                    .username(user.getUsername())
                    .build();

        } catch (Exception e) {
            log.error("Authentication failed for user: {}", request.username(), e);
            loginService.loginFailed(request.username());
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public AuthResponseDTO refreshToken(String refreshToken) {
        var token = refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new BadCredentialsException("Refresh token not found"));
        var verifiedToken = refreshTokenService.verifyExpiration(token);

        if (verifiedToken.isRevoked()) {
            log.warn("Attempt to use revoked refresh token for user: {}",
                    verifiedToken.getUser().getUsername());
            throw new BadCredentialsException("Refresh token is revoked");
        }

        var user = verifiedToken.getUser();
        var newAccessToken = jwtService.generateAccessToken(user);

        log.info("Access token refreshed for user: {}", user.getUsername());

        return AuthResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtService.extractExpiration(newAccessToken).getTime())
                .role(user.getRole().name())
                .username(user.getUsername())
                .build();

    }

    public void logout(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String refreshToken = authHeader.substring(7);
            refreshTokenService.revokeRefreshToken(refreshToken);
            log.info("User logged out, refresh token revoked");
        } else {
            throw new BadCredentialsException("Invalid authentication header");
        }
    }

    public void createInitialUsers() {
        if (!userDAOService.existsByUsername("user")) {
            var user = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.USER)
                    .build();
            userDAOService.saveUser(user);
            log.info("Default USER created: user/password");
        }

        if (!userDAOService.existsByUsername("moderator")) {
            var moderator = User.builder()
                    .username("moderator")
                    .password(passwordEncoder.encode("moder123"))
                    .role(Role.MODERATOR)
                    .build();
            userDAOService.saveUser(moderator);
            log.info("Default MODERATOR created: moderator/moder123");
        }

        if (!userDAOService.existsByUsername("admin")) {
            var admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.SUPER_ADMIN)
                    .build();
            userDAOService.saveUser(admin);
            log.info("Default SUPER_ADMIN created: admin/admin123");
        }
    }

}
