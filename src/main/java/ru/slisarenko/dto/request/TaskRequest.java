package ru.slisarenko.dto.request;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.slisarenko.enums.TaskPriority;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TaskRequest {
    private UUID projectId;
    private String title;
    private String description;
    private TaskPriority priority;
    private Instant dueDate;
    private UUID assigneeId;
}
