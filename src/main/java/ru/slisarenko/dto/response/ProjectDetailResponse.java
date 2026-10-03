package ru.slisarenko.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectDetailResponse {
    private ProjectResponse project;
    private List<ProjectMemberResponse> members;
    private long tasksCount;
}
