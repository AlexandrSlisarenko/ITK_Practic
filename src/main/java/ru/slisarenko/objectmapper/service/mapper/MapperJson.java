package ru.slisarenko.objectmapper.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.slisarenko.objectmapper.service.dto.CustomerCreateDTO;
import ru.slisarenko.objectmapper.service.dto.CustomerResponseDTO;
import ru.slisarenko.objectmapper.service.dto.OrderRequestDTO;
import ru.slisarenko.objectmapper.service.dto.OrderResponseDTO;
import ru.slisarenko.objectmapper.service.dto.ProductDTO;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class MapperJson {
    private final ObjectMapper objectMapper;

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
        return objectMapper.readValue(json, ProductDTO.class);
    }

    public String serializeOrderResponseDto(OrderResponseDTO dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    public OrderRequestDTO deserializeOrderRequestDto(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, OrderRequestDTO.class);
    }

    public String serializeCustomerResponseDto(CustomerResponseDTO dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    public CustomerCreateDTO deserializeCustomerCreateDto(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, CustomerCreateDTO.class);
    }
}
