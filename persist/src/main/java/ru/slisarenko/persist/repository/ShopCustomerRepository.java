package ru.slisarenko.persist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.slisarenko.persist.entity.ShopCustomer;

@Repository
public interface ShopCustomerRepository extends JpaRepository<ShopCustomer, Long> {
}
