package ru.slisarenko.objectmapper.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.slisarenko.objectmapper.service.dto.CustomerCreateDTO;
import ru.slisarenko.objectmapper.service.dto.CustomerRequestDTO;
import ru.slisarenko.objectmapper.service.dto.CustomerResponseDTO;
import ru.slisarenko.objectmapper.service.dto.OrderRequestDTO;
import ru.slisarenko.objectmapper.service.dto.OrderResponseDTO;
import ru.slisarenko.objectmapper.service.dto.ProductDTO;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class MapperJson {
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public String serializeProductDto(ProductDTO dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    public String serializeListProductDto(List<ProductDTO> dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    public String serializeListProductDto(Page<ProductDTO> dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    public ProductDTO deserializeProductDto(String json) throws JsonProcessingException {
        var dto = objectMapper.readValue(json, ProductDTO.class);
        validate(dto);
        return dto;
    }

    public String serializeOrderResponseDto(OrderResponseDTO dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    public OrderRequestDTO deserializeOrderRequestDto(String json) throws JsonProcessingException {
        var dto = objectMapper.readValue(json, OrderRequestDTO.class);
        validate(dto);
        return dto;
    }

    public String serializeCustomerResponseDto(CustomerResponseDTO dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    public CustomerCreateDTO deserializeCustomerCreateDto(String json) throws JsonProcessingException {
        var dto = objectMapper.readValue(json, CustomerCreateDTO.class);
        validate(dto);
        return dto;
    }
    public CustomerRequestDTO deserializeCustomerRequestDto(String json) throws JsonProcessingException {
        var dto = objectMapper.readValue(json, CustomerRequestDTO.class);
        validate(dto);
        return dto;
    }

    private void validate(CustomerCreateDTO dto) {
        Set<ConstraintViolation<CustomerCreateDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private void validate(CustomerRequestDTO dto) {
        Set<ConstraintViolation<CustomerRequestDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private void validate(OrderRequestDTO dto) {
        Set<ConstraintViolation<OrderRequestDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private void validate(ProductDTO dto) {
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }


}
