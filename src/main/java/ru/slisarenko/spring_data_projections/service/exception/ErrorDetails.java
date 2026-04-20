package ru.slisarenko.spring_data_projections.service.exception;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ErrorDetails(LocalDateTime timestamp, String message, String details) {
}
