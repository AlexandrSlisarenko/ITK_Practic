package ru.slisarenko.entity_library.dto.order;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record OrderRequestDTO(@NonNull Long customerId,
                              @NonNull List<Long> productIds) {
}
