package ru.slisarenko.spring_security_jwt.service;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.slisarenko.spring_security_jwt.logger.AuthenticationLogger;
import ru.slisarenko.spring_security_jwt.repository.UserRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {

    private final UserDAOService userDAOService;
    private final AuthenticationLogger authLogger;

    @Value("${security.max-login-attempts:5}")
    private int maxAttempts;

    @Value("${security.lockout-duration:30}")
    private int lockoutDuration;

    public void loginSucceeded(String username) {
        userDAOService.findByUsername(username).ifPresent(user -> {
            if(!user.isAccountNonLocked()){
                if (user.getLockTime() != null &&
                    user.getLockTime().plusMinutes(lockoutDuration).isBefore(LocalDateTime.now())) {
                    user.unlockAccount();
                    log.info("Account automatically unlocked for user: {}", username);
                    authLogger.logAccountUnlocked(username, "SYSTEM");
                }
            }
            user.resetFailedAttempts();
            userDAOService.saveUser(user);
            var ipAddress = getClientIp();
            authLogger.logSuccessfulLogin(username, user.getRole().name(), ipAddress, getClientUserAgent());
        });
    }

    public void unlockAccount(String username) {
        userDAOService.unlockAccount(username);
        log.info("Account manually unlocked for user: {}", username);
        authLogger.logAccountUnlocked(username, "ADMIN");
    }

    public void loginFailed(String username) {
        var ipAddress = getClientIp();
        userDAOService.findByUsername(username).ifPresentOrElse(user -> {
            user.incrementFailedAttempts();
            log.warn("Failed login attempt {} for user: {}", user.getFailedAttempts(), username);
            authLogger.logFailedLogin(username, "Invalid password", ipAddress, maxAttempts - user.getFailedAttempts());
            if (user.getFailedAttempts() >= maxAttempts) {
                user.lockAccount();
                userDAOService.saveUser(user);
                log.error("Account locked for user: {} due to {} failed attempts", username, maxAttempts);
                authLogger.logAccountLocked(username, user.getFailedAttempts(), ipAddress);
            } else {
                userDAOService.saveUser(user);
            }
        }, () -> {

            authLogger.logFailedLogin(username, "User not found", ipAddress, 0);
        });

    }

    private String getClientIp() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) return "unknown";

        HttpServletRequest request = attributes.getRequest();
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    private String getClientUserAgent() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) return "unknown";

        HttpServletRequest request = attributes.getRequest();
        return request.getHeader("User-Agent") != null ? request.getHeader("User-Agent") : "unknown";
    }
}
