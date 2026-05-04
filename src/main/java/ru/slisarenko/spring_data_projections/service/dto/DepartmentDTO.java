package ru.slisarenko.spring_data_projections.service.dto;


import lombok.Builder;

@Builder
public record DepartmentDTO(Long id,
                            String name) {
}
