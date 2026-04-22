package ru.slisarenko.objectmapper.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import ru.slisarenko.objectmapper.model.enums.OrderStatus;

@Builder
public record OrderResponseDTO(Long orderId,
                               Long customerId,
                               List<Long> productIds,
                               LocalDateTime orderDate,
                               String shippingAddress,
                               BigDecimal totalPrice,
                               OrderStatus orderStatus
                               ) {
}
