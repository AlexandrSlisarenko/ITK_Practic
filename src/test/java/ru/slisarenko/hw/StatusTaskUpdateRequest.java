package ru.slisarenko.hw;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class StatusTaskUpdateRequest {
    private TaskStatus status;
    private Integer version;
}
