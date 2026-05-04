package ru.slisarenko.jsonview.model.service;

import java.util.Optional;
import ru.slisarenko.jsonview.model.entity.Product;


public interface DAOProductService {
    Product saveOrUpdate(Product entity);
    Optional<Product> findProductById(Long id);
}
