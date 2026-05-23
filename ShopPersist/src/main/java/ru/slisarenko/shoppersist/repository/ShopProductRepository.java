package ru.slisarenko.shoppersist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.slisarenko.shoppersist.entity.ShopProduct;

public interface ShopProductRepository extends JpaRepository<ShopProduct, Long> {
}
