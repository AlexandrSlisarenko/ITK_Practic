package ru.slisarenko.jsonview.service.dto;


import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record ProductDTO(String name, BigDecimal price) {
}
