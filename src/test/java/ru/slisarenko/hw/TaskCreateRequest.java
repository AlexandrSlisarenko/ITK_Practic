package ru.slisarenko.hw;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TaskCreateRequest {

    private UUID projectId;
    private String title;
    private String description;
    private TaskPriority priority;
    private Instant dueDate;
    private UUID assigneeId;
}
