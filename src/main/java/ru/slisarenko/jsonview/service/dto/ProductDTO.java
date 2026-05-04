package ru.slisarenko.jsonview.service.dto;


import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record ProductDTO(Long id,
                         String name,
                         BigDecimal price) {
}
