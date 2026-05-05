package ru.slisarenko.spring_security_jwt.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.slisarenko.spring_security_jwt.repository.UserRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {

    private final UserDAOService userDAOService;

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
                }
            }
            user.resetFailedAttempts();
            userDAOService.saveUser(user);
        });
    }

    public void unlockAccount(String username) {
        userDAOService.unlockAccount(username);
        log.info("Account manually unlocked for user: {}", username);
    }

    public void loginFailed(String username) {
        userDAOService.findByUsername(username).ifPresent(user -> {
            user.incrementFailedAttempts();
            log.warn("Failed login attempt {} for user: {}", user.getFailedAttempts(), username);
            if (user.getFailedAttempts() >= maxAttempts) {
                user.lockAccount();
                userDAOService.saveUser(user);
                log.error("Account locked for user: {} due to {} failed attempts", username, maxAttempts);
            } else {
                userDAOService.saveUser(user);
            }
        });

    }
}
