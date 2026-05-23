package ru.slisarenko.entity_lobrary.dto.order;

import java.util.List;
import lombok.Builder;

@Builder
public record OrderRequestDTO(Long customerId,
                              List<Long> productIds) {
}
