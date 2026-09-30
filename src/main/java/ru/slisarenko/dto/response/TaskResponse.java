package ru.slisarenko.dto.response;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.slisarenko.enums.TaskPriority;
import ru.slisarenko.enums.TaskStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class TaskResponse {
    private UUID id;
    private UUID projectId;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private UUID assigneeId;
    private Instant dueDate;
    private Long version;
    private Instant createdAt;
    private Instant updatedAt;
}
