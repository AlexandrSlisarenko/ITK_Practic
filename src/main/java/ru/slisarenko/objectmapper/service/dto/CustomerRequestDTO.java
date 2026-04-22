package ru.slisarenko.objectmapper.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;

@Builder
public record CustomerRequestDTO(@Email
                                 @Value("default@mail.ru")
                                 String email,
                                 @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")
                                 String contactNumber) {
}
