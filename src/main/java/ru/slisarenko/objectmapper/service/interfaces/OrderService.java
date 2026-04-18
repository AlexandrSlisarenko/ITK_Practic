package ru.slisarenko.objectmapper.service.interfaces;

import ru.slisarenko.objectmapper.service.dto.OrderRequestDTO;
import ru.slisarenko.objectmapper.service.dto.OrderResponseDTO;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO);
    OrderResponseDTO getOrder(Long orderId);
}
