package ru.slisarenko.spring_data_projections.service.dto;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record EmployeeRequestDTO(String firstName,
                                 String lastName,
                                 String position,
                                 BigDecimal salary,
                                 Long departmentId) {
}
