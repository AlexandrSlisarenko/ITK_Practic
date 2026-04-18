package ru.slisarenko.objectmapper.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.slisarenko.objectmapper.model.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
