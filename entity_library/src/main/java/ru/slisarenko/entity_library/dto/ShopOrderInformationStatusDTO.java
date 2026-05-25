package ru.slisarenko.entity_library.dto;

import lombok.Builder;
import ru.slisarenko.entity_library.enums.OrderStatus;

@Builder
public record ShopOrderInformationStatusDTO(Long requestId,
                                            Long orderId,
                                            OrderStatus status) {
}
