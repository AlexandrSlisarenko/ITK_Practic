package ru.slisarenko.jsonview.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.slisarenko.jsonview.model.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
