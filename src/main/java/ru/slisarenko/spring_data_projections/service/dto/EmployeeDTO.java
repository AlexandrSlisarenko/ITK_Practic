package ru.slisarenko.spring_data_projections.service.dto;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record EmployeeDTO(Long id,
                          String firstName,
                          String lastName,
                          String position,
                          BigDecimal salary,
                          DepartmentDTO department) {
}
