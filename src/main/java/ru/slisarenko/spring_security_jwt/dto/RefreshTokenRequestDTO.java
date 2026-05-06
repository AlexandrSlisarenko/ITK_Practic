package ru.slisarenko.spring_security_jwt.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RefreshTokenRequestDTO(@NotBlank(message = "Refresh token is required") String refreshToken) {
}
