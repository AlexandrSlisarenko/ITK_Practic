package ru.slisarenko.objectmapper.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

@Builder
public record OrderRequestDTO(@NotNull(message = "Customer email or contact number is required")
                              String emailOrContactNumber,

                              @NotEmpty(message="At least one product is required")
                              List<Long> productIds,

                              @NotBlank(message = "Shipping address is required")
                              String shippingAddress){
}
