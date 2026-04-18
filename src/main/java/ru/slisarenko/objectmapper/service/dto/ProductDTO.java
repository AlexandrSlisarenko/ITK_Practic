package ru.slisarenko.objectmapper.service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record ProductDTO(Long productId,

                         @NotBlank(message = "Product name is required")
                         String name,

                         @NotBlank(message = "Product name is required")
                         String description,

                         @NotNull(message = "Price is required")
                         @Positive(message = "Price must be positive")
                         BigDecimal price,

                         @NotNull(message = "Quantity in stock is required")
                         @Min(value = 0, message = "Quantity cannot be negative")
                         Integer quantityInStock) {
}
