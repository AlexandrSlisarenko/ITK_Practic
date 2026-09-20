package ru.slisarenko.dto.response;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.slisarenko.enums.ProjectRole;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectMemberResponse {
    private UUID id;
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
    private ProjectRole role;
    private Instant joinedAt;
}
