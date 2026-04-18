package ru.slisarenko.objectmapper.service.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;

@Builder
public record CustomerRequestDTO(@Email String email,
                                 String contactNumber) {
}
