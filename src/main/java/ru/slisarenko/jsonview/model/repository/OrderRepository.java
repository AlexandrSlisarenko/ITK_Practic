package ru.slisarenko.jsonview.model.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.slisarenko.jsonview.model.entity.Order;
import ru.slisarenko.jsonview.service.dto.OrderDTO;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT NEW ru.slisarenko.jsonview.service.dto.OrderDTO(orderEntity.id, orderEntity.status, orderEntity.totalPrice) " +
           "FROM Order orderEntity " +
           "WHERE orderEntity.customer.id = :id")
    List<OrderDTO> findStatusAndTotalPriceOrderItemsByCustomerId(@Param("id") Long id);
}
