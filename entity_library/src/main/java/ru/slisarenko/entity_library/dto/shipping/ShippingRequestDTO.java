package ru.slisarenko.entity_library.dto.shipping;

public record ShippingRequestDTO(Long requestId,
                                 Long orderId,
                                 Long customerId,
                                 String address) {
}
