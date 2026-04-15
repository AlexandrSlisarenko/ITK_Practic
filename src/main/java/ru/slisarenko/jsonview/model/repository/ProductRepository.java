package ru.slisarenko.jsonview.model.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import ru.slisarenko.jsonview.model.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByOrder_Id(Long orderId);
}
