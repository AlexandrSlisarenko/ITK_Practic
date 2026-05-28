package ru.slisarenko.persist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.slisarenko.persist.entity.ShopProduct;

public interface ShopProductRepository extends JpaRepository<ShopProduct, Long> {
}
