package ru.slisarenko.entity_lobrary.dto;

import lombok.Builder;
import ru.slisarenko.entity_lobrary.enums.OrderStatus;

@Builder
public record ShopOrderInformationStatusDTO(Long requestId,
                                            Long orderId,
                                            OrderStatus status) {
}
