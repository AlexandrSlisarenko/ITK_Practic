package ru.slisarenko.persist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.slisarenko.persist.entity.ShopOrder;

@Repository
public interface ShopOrderRepository extends JpaRepository<ShopOrder, Long> {
}
