package ru.slisarenko.spring_security_jwt.logger;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationLogger {

    private final ObjectMapper objectMapper;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Логирование успешной аутентификации
     */
    public void logSuccessfulLogin(String username, String role, String ipAddress, String userAgent) {
        ObjectNode logEntry = objectMapper.createObjectNode();
        logEntry.put("timestamp", LocalDateTime.now().format(formatter));
        logEntry.put("eventType", "LOGIN_SUCCESS");
        logEntry.put("username", username);
        logEntry.put("role", role);
        logEntry.put("ipAddress", ipAddress);
        logEntry.put("userAgent", userAgent);
        logEntry.put("status", "SUCCESS");

        log.info("AUTH_SUCCESS: {}", logEntry.toString());
    }

    /**
     * Логирование неудачной аутентификации
     */
    public void logFailedLogin(String username, String reason, String ipAddress, int attemptsRemaining) {
        ObjectNode logEntry = objectMapper.createObjectNode();
        logEntry.put("timestamp", LocalDateTime.now().format(formatter));
        logEntry.put("eventType", "LOGIN_FAILED");
        logEntry.put("username", username != null ? username : "unknown");
        logEntry.put("reason", reason);
        logEntry.put("ipAddress", ipAddress);
        logEntry.put("attemptsRemaining", attemptsRemaining);
        logEntry.put("status", "FAILED");

        log.warn("AUTH_FAILURE: {}", logEntry.toString());
    }

    /**
     * Логирование блокировки аккаунта
     */
    public void logAccountLocked(String username, int failedAttempts, String ipAddress) {
        ObjectNode logEntry = objectMapper.createObjectNode();
        logEntry.put("timestamp", LocalDateTime.now().format(formatter));
        logEntry.put("eventType", "ACCOUNT_LOCKED");
        logEntry.put("username", username);
        logEntry.put("failedAttempts", failedAttempts);
        logEntry.put("ipAddress", ipAddress);
        logEntry.put("action", "LOCK");

        log.error("SECURITY_ALERT: {}", logEntry.toString());
    }

    /**
     * Логирование разблокировки аккаунта
     */
    public void logAccountUnlocked(String username, String unlockedBy) {
        ObjectNode logEntry = objectMapper.createObjectNode();
        logEntry.put("timestamp", LocalDateTime.now().format(formatter));
        logEntry.put("eventType", "ACCOUNT_UNLOCKED");
        logEntry.put("username", username);
        logEntry.put("unlockedBy", unlockedBy);
        logEntry.put("action", "UNLOCK");

        log.info("SECURITY: {}", logEntry.toString());
    }

    /**
     * Логирование генерации JWT токена
     */
    public void logTokenGenerated(String username, String tokenType, long expiresIn) {
        ObjectNode logEntry = objectMapper.createObjectNode();
        logEntry.put("timestamp", LocalDateTime.now().format(formatter));
        logEntry.put("eventType", "TOKEN_GENERATED");
        logEntry.put("username", username);
        logEntry.put("tokenType", tokenType);
        logEntry.put("expiresIn", expiresIn);
        logEntry.put("action", "TOKEN_CREATED");

        log.debug("TOKEN: {}", logEntry.toString());
    }

    /**
     * Логирование обновления токена
     */
    public void logTokenRefreshed(String username, String oldTokenId, String newTokenId) {
        ObjectNode logEntry = objectMapper.createObjectNode();
        logEntry.put("timestamp", LocalDateTime.now().format(formatter));
        logEntry.put("eventType", "TOKEN_REFRESHED");
        logEntry.put("username", username);
        logEntry.put("oldTokenId", oldTokenId);
        logEntry.put("newTokenId", newTokenId);

        log.info("TOKEN_REFRESH: {}", logEntry.toString());
    }

    /**
     * Логирование валидации токена
     */
    public void logTokenValidation(String username, boolean isValid, String reason) {
        ObjectNode logEntry = objectMapper.createObjectNode();
        logEntry.put("timestamp", LocalDateTime.now().format(formatter));
        logEntry.put("eventType", "TOKEN_VALIDATION");
        logEntry.put("username", username);
        logEntry.put("isValid", isValid);
        logEntry.put("reason", reason != null ? reason : "N/A");

        if (!isValid) {
            log.warn("TOKEN_INVALID: {}", logEntry.toString());
        } else {
            log.debug("TOKEN_VALID: {}", logEntry.toString());
        }
    }

    /**
     * Логирование доступа к ресурсу
     */
    public void logResourceAccess(String username, String role, String endpoint, String method,
                                  String ipAddress, boolean isAllowed) {
        ObjectNode logEntry = objectMapper.createObjectNode();
        logEntry.put("timestamp", LocalDateTime.now().format(formatter));
        logEntry.put("eventType", "RESOURCE_ACCESS");
        logEntry.put("username", username);
        logEntry.put("role", role);
        logEntry.put("endpoint", endpoint);
        logEntry.put("method", method);
        logEntry.put("ipAddress", ipAddress);
        logEntry.put("isAllowed", isAllowed);

        if (!isAllowed) {
            log.warn("ACCESS_DENIED: {}", logEntry.toString());
        } else {
            log.debug("ACCESS_GRANTED: {}", logEntry.toString());
        }
    }

    /**
     * Логирование логаута
     */
    public void logLogout(String username, String ipAddress) {
        ObjectNode logEntry = objectMapper.createObjectNode();
        logEntry.put("timestamp", LocalDateTime.now().format(formatter));
        logEntry.put("eventType", "LOGOUT");
        logEntry.put("username", username);
        logEntry.put("ipAddress", ipAddress);
        logEntry.put("action", "LOGOUT");

        log.info("AUTH_LOGOUT: {}", logEntry.toString());
    }
}
