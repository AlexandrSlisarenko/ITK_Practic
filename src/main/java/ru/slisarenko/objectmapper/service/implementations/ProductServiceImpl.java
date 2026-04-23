package ru.slisarenko.objectmapper.service.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.slisarenko.objectmapper.model.entity.Product;
import ru.slisarenko.objectmapper.model.repository.ProductRepository;
import ru.slisarenko.objectmapper.service.dto.ProductDTO;
import ru.slisarenko.objectmapper.service.exception.NotFoundProduct;
import ru.slisarenko.objectmapper.service.interfaces.ProductService;
import ru.slisarenko.objectmapper.service.mapper.MapperJson;
import ru.slisarenko.objectmapper.service.mapper.ProductMapper;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final MapperJson mapperJson;

    @Override
    public String getProducts(int page, int size) throws JsonProcessingException {
        Pageable pageable = PageRequest.of(page, size);
        var products = this.productRepository.findAll(pageable).stream()
                .map(productMapper::toDTO)
                .toList();
        Page<ProductDTO> orderPage = new PageImpl<>(products, pageable, products.size());
        return mapperJson.serializeListProductDto(orderPage);
    }

    @Override
    public String getProduct(Long id) throws JsonProcessingException {
        var product = this.productRepository.findById(id).orElseThrow(() -> new NotFoundProduct(id));
        var dto = this.productMapper.toDTO(product);
        return this.mapperJson.serializeProductDto(dto);
    }

    @Override
    public Optional<Product> getProductToCreateOrder(Long id) {
        var product = this.productRepository.findById(id).orElseThrow(() -> new NotFoundProduct(id));
        return Optional.of(product);
    }

    @Transactional
    @Override
    public String createProduct(String productIn) throws JsonProcessingException {
        var requestDto = this.mapperJson.deserializeProductDto(productIn);
        var product = this.productMapper.toEntity(requestDto);
        product = this.productRepository.save(product);
        var responseDTO = this.productMapper.toDTO(product);
        return this.mapperJson.serializeProductDto(responseDTO);
    }

    @Override
    public String updateProduct(Long id, String productIn) throws JsonProcessingException {
        var productFromDB = this.productRepository.findById(id)
                .orElseThrow(() -> new NotFoundProduct(id));
        var requestDto = this.mapperJson.deserializeProductDto(productIn);
        var product = this.productMapper.toEntity(requestDto);
        productFromDB = this.productMapper.updateProduct(product, productFromDB);
        product = this.productRepository.save(productFromDB);
        var responseDTO = this.productMapper.toDTO(product);
        return this.mapperJson.serializeProductDto(responseDTO);
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
