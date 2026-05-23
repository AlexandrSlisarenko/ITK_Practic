package ru.slisarenko.entity_lobrary.dto.shipping;

public record ShippingRequestDTO(Long id,
                                 Long orderId,
                                 Long customerId,
                                 String address) {
}
