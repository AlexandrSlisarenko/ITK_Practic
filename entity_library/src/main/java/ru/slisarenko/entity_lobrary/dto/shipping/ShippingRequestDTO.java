package ru.slisarenko.entity_lobrary.dto.shipping;

public record ShippingRequestDTO(Long requestId,
                                 Long orderId,
                                 Long customerId,
                                 String address) {
}
