package ru.slisarenko.spring_security_jwt.dto;

import lombok.Builder;

@Builder
public record AuthResponse(String accessToken,
                           String refreshToken,
                           String tokenType,
                           String role,
                           String username,
                           Long expiresIn) {
}
