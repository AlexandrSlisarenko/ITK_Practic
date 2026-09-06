package ru.slisarenko.hw;

import groovy.transform.builder.Builder;
import java.time.Instant;
import java.util.UUID;

@Builder
public record TaskResponse(
        UUID id,
        UUID projectId,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        UUID assigneeId,
        Instant dueDate,
        Long version,
        Instant createdAt,
        Instant updatedAt
) {
}

