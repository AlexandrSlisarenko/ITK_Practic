package ru.slisarenko.spring_data_jdbc.exceptions;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ErrorDetails(LocalDateTime timestamp, String message, String details) {
}
