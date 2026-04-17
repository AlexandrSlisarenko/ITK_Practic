package ru.slisarenko.jsonview.exceptions;

import java.io.Serializable;
import lombok.Builder;

@Builder
public record ErrorResponse(Integer status,
                            String message,
                            String error) implements Serializable {
}
