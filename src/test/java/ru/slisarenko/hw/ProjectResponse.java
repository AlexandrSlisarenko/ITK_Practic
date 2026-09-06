package ru.slisarenko.hw;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProjectResponse{
    private UUID id;
    private String name;
    private String description;
    private String key;
    private boolean archived;
    private UUID ownerId;
    private Instant createdAt;
    private Instant updatedAt;
}
