package ru.slisarenko.objectmapper.service.dto;

import jakarta.validation.constraints.NotNull;

public record CustomerCreateDTO(@NotNull(message = "Customer firstName is required")
                                        String firstName,
                                @NotNull(message = "Customer lastName is required")
                                        String lastName,
                                @NotNull(message = "Customer email is required")
                                        String email,
                                @NotNull(message = "Customer contactNumber is required")
                                        String contactNumber) {
}
