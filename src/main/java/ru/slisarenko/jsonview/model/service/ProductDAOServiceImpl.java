package ru.slisarenko.jsonview.model.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.slisarenko.jsonview.model.entity.Product;
import ru.slisarenko.jsonview.model.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductDAOServiceImpl implements DAOService<Product> {

    private ProductRepository productRepository;

    @Override
    public Optional<Product> getById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getAll(Long id) {
        return productRepository.findByOrder_Id(id);
    }
}
