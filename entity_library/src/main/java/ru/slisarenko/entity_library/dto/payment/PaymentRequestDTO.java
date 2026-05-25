package ru.slisarenko.entity_library.dto.payment;

import lombok.Builder;

@Builder
public record PaymentRequestDTO(Long requestId,
                                Long orderId,
                                Long customerId) {
}
