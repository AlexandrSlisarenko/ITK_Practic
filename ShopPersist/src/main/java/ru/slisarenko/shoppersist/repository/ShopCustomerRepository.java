package ru.slisarenko.shoppersist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.slisarenko.shoppersist.entity.ShopCustomer;

@Repository
public interface ShopCustomerRepository extends JpaRepository<ShopCustomer, Long> {
}
