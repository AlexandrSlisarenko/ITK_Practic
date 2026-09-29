package ru.slisarenko.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TaskRequest {
    private String  title;
    private String  description;
    private String  priority;
    private String dueDate;
    private String assigneeId;
}
