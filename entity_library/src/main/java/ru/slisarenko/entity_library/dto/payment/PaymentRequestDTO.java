package ru.slisarenko.entity_library.dto.payment;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record PaymentRequestDTO(@NonNull String requestUUId,
                                @NonNull Long orderId,
                                @NonNull Long customerId) {
}
