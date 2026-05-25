package ru.slisarenko.entity_library.dto.shipping;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record ShippingRequestDTO(@NonNull String requestUUId,
                                 @NonNull Long orderId,
                                 @NonNull Long customerId,
                                 @NonNull String address) {
}
