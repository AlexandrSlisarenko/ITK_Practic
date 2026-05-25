package ru.slisarenko.entity_library.dto.persist;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.NonNull;
import ru.slisarenko.entity_library.enums.OrderStatus;

@Builder
public record PersistDTO(@NonNull Long requestId ,
                         UUID orderUUID,
                         Long customerId,
                         List<Long> productIds,
                         Long orderId,
                         OrderStatus status) {
}
