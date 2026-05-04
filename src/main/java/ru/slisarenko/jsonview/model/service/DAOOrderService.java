package ru.slisarenko.jsonview.model.service;

import java.util.List;
import ru.slisarenko.jsonview.service.dto.OrderDTO;

public interface DAOOrderService {
    List<OrderDTO> getOrderItems(Long customerId);
}
