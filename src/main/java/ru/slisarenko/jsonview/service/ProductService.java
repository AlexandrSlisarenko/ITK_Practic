package ru.slisarenko.jsonview.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.slisarenko.jsonview.exceptions.ProductNotFoundException;
import ru.slisarenko.jsonview.model.repository.ProductRepository;
import ru.slisarenko.jsonview.model.service.DAOProductService;
import ru.slisarenko.jsonview.service.dto.ProductDTO;
import ru.slisarenko.jsonview.service.mapper.ProductMapper;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final DAOProductService daoProductService;
    private final ProductMapper productMapper;

    public ProductDTO saveProduct(ProductDTO productDTO) {
        var product = productMapper.toModel(productDTO);
        product = this.daoProductService.saveOrUpdate(product);
        return productMapper.toDTO(product);
    }

    public ProductDTO getProductById(Long id) {
        var product = this.daoProductService.findProductById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toDTO(product);
    }

}
