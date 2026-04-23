package ru.slisarenko.objectmapper.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CustomerCreateDTO(@NotBlank(message = "Customer firstName is required")
                                        String firstName,
                                @NotBlank(message = "Customer lastName is required")
                                        String lastName,
                                @Email(message = "Customer email is required")
                                        String email,
                                @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$", message = "Customer contactNumber is required")
                                        String contactNumber) {
}
