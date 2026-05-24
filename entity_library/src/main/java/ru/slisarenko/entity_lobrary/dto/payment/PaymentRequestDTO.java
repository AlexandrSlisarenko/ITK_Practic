package ru.slisarenko.entity_lobrary.dto.payment;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record PaymentRequestDTO(Long requestId,
                                Long orderId,
                                Long customerId) {
}
