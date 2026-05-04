package ru.slisarenko.jsonview.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.slisarenko.jsonview.model.entity.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
}

