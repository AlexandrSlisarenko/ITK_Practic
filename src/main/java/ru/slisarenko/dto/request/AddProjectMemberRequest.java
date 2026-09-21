package ru.slisarenko.dto.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.slisarenko.enums.ProjectRole;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AddProjectMemberRequest {
    private UUID userId;
    private ProjectRole role;
}
