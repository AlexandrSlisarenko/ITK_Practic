package ru.slisarenko.shoppersist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.slisarenko.shoppersist.entity.ShopOrder;

@Repository
public interface ShopOrderRepository extends JpaRepository<ShopOrder, Long> {
}
