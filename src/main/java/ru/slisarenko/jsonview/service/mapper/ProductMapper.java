package ru.slisarenko.jsonview.service.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.slisarenko.jsonview.model.entity.Product;
import ru.slisarenko.jsonview.service.dto.ProductDTO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true)
)
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    Product toModel(ProductDTO product);

    ProductDTO toDTO(Product product);
}
