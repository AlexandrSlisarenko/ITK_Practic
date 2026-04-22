package ru.slisarenko.objectmapper.service.dto;

import java.util.List;
import lombok.Builder;
import ru.slisarenko.objectmapper.model.entity.Order;

@Builder
public record CustomerResponseDTO(Long customerId,
                                  String firstName,
                                  String lastName,
                                  String email,
                                  String contactNumber,
                                  List<Long> orders) {
}
