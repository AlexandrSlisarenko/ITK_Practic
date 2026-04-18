package ru.slisarenko.objectmapper.service.implementations;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.slisarenko.objectmapper.model.entity.Product;
import ru.slisarenko.objectmapper.model.repository.ProductRepository;
import ru.slisarenko.objectmapper.service.dto.ProductDTO;
import ru.slisarenko.objectmapper.service.exception.NotFoundProduct;
import ru.slisarenko.objectmapper.service.interfaces.ProductService;
import ru.slisarenko.objectmapper.service.mapper.ProductMapper;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductDTO> getProducts() {
        return this.productRepository.findAll().stream()
                .map(productMapper::toDTO)
                .toList();
    }

    @Override
    public ProductDTO getProduct(Long id) {
        var product = this.productRepository.findById(id).orElseThrow(() -> new NotFoundProduct(id));
        return this.productMapper.toDTO(product);
    }

    @Override
    public Optional<Product> getProductToCreateOrder(Long id) {
        var product = this.productRepository.findById(id).orElseThrow(() -> new NotFoundProduct(id));
        return Optional.of(product);
    }

    @Transactional
    @Override
    public ProductDTO createOrUpdate(ProductDTO productIn) {
        var product = this.productMapper.toEntity(productIn);
        if (productIn.productId() != null) {
            var productFromDB = this.productRepository.findById(product.getProductId())
                    .orElseThrow(() -> new NotFoundProduct(productIn.productId()));
            productFromDB = this.productMapper.updateProduct(product, productFromDB);
            product = this.productRepository.save(productFromDB);
        } else {
            product = this.productRepository.save(product);
        }
        return this.productMapper.toDTO(product);
    }

    @Override
    public boolean deleteProduct(Long id) {
        if (this.productRepository.existsById(id)) {
            this.productRepository.deleteById(id);
            return true;
        } else {
            throw new NotFoundProduct(id);
        }
    }

    public ProductDTO mappingProductToDTO(Product product){
        return this.productMapper.toDTO(product);
    }
}
