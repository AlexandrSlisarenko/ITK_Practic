package ru.slisarenko.objectmapper.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import ru.slisarenko.objectmapper.model.entity.Customer;
import ru.slisarenko.objectmapper.model.enums.OrderStatus;

@Builder
public record OrderResponseDTO(Long orderId,
                               Customer customer,
                               List<ProductDTO> products,
                               LocalDateTime orderDate,
                               String shippingAddress,
                               BigDecimal totalPrice,
                               OrderStatus orderStatus
                               ) {
}
