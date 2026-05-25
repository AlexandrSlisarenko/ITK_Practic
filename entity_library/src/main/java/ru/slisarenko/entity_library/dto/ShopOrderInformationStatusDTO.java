package ru.slisarenko.entity_library.dto;

import lombok.Builder;
import lombok.NonNull;
import ru.slisarenko.entity_library.enums.OrderStatus;

@Builder
public record ShopOrderInformationStatusDTO(@NonNull String requestUUId,
                                            @NonNull Long orderId,
                                            @NonNull OrderStatus status) {
}
