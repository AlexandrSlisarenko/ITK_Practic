package ru.slisarenko.objectmapper.service.interfaces;

import java.util.List;
import java.util.Optional;
import ru.slisarenko.objectmapper.model.entity.Product;
import ru.slisarenko.objectmapper.service.dto.ProductDTO;

public interface ProductService {
    List<ProductDTO> getProducts();
    ProductDTO getProduct(Long id);
    ProductDTO createOrUpdate(ProductDTO productIn);
    boolean deleteProduct(Long id);
    Optional<Product> getProductToCreateOrder(Long id);
    ProductDTO mappingProductToDTO(Product product);
}
