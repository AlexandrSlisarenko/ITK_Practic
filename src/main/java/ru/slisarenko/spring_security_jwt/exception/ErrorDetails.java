package ru.slisarenko.spring_security_jwt.exception;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ErrorDetails(LocalDateTime timestamp,
                           String message,
                           String details,
                           String url) {
}
