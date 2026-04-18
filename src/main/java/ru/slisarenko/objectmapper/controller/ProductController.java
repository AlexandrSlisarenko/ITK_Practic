package ru.slisarenko.objectmapper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.slisarenko.objectmapper.service.dto.ProductDTO;
import ru.slisarenko.objectmapper.service.interfaces.ProductService;
import ru.slisarenko.objectmapper.service.mapper.MapperJson;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final MapperJson mapperJson;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<String> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int size) throws JsonProcessingException{

        Pageable pageable = PageRequest.of(page, size);
        var products = productService.getProducts();
        Page<ProductDTO> orderPage = new PageImpl<>(products, pageable, products.size());
        var json = mapperJson.serializeListProductDto(orderPage);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProduct(@PathVariable Long id) throws JsonProcessingException {
        var json = this.mapperJson.serializeProductDto(productService.getProduct(id));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@Valid @RequestBody String jsonRequestDto) throws JsonProcessingException {
        var requestDto = this.mapperJson.deserializeProductDto(jsonRequestDto);
        var created = productService.createOrUpdate(requestDto);
        var json = this.mapperJson.serializeProductDto(created);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @PutMapping
    public ResponseEntity<String> updateProduct(@Valid @RequestBody String jsonRequestDto) throws JsonProcessingException {
        var requestDto = this.mapperJson.deserializeProductDto(jsonRequestDto);
        var json = this.mapperJson.serializeProductDto(productService.createOrUpdate(requestDto));
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
