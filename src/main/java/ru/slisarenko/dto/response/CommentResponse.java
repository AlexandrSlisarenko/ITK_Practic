package ru.slisarenko.dto.response;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentResponse{
        private UUID id;
        private UUID taskId;
        private UUID authorId;
        private String authorName;
        private String body;
        private Long version;
        private Instant createdAt;
        private Instant updatedAt;
}
