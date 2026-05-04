package ru.slisarenko.jsonview.model.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.slisarenko.jsonview.model.repository.OrderRepository;
import ru.slisarenko.jsonview.service.dto.OrderDTO;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DAOOrderServiceImpl implements DAOOrderService {
    private final OrderRepository orderRepository;

    @Override
    public List<OrderDTO> getOrderItems(Long customerId) {
        return this.orderRepository.findStatusAndTotalPriceOrderItemsByCustomerId(customerId);
    }
}
