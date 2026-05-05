package ru.slisarenko.spring_security_jwt.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthRequest(@NotBlank(message = "Username is required") String username,
                          @NotBlank(message = "Password is required") String password) {
}
