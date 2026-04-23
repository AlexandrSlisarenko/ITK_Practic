package ru.slisarenko.objectmapper.service.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.slisarenko.objectmapper.service.dto.OrderRequestDTO;
import ru.slisarenko.objectmapper.service.dto.OrderResponseDTO;

public interface OrderService {
    String createOrder(String orderRequestDTO) throws JsonProcessingException;
    String getOrder(Long orderId) throws JsonProcessingException;
}
