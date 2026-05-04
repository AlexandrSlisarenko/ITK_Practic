package ru.slisarenko.jsonview.model.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.slisarenko.jsonview.model.entity.Product;
import ru.slisarenko.jsonview.model.repository.ProductRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class DAOProductServiceImpl implements DAOProductService {
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<Product> findProductById(Long id) {
        return this.productRepository.findById(id);
    }

    @Override
    public Product saveOrUpdate(Product entity) {
        return this.productRepository.save(entity);
    }
}
