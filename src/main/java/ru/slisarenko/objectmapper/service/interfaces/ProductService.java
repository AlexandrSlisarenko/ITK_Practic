package ru.slisarenko.objectmapper.service.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Optional;
import ru.slisarenko.objectmapper.model.entity.Product;
import ru.slisarenko.objectmapper.service.dto.ProductDTO;

public interface ProductService {
    String getProducts(int page, int size) throws JsonProcessingException;
    String getProduct(Long id) throws JsonProcessingException;
    String createProduct(String productIn) throws JsonProcessingException;
    String updateProduct(Long id, String productIn) throws JsonProcessingException;
    boolean deleteProduct(Long id);
    Optional<Product> getProductToCreateOrder(Long id);
    ProductDTO mappingProductToDTO(Product product);
}
