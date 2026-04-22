package ru.slisarenko.objectmapper.service.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.slisarenko.objectmapper.model.entity.Product;
import ru.slisarenko.objectmapper.service.dto.ProductDTO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true)
)
public interface ProductMapper {
    public abstract ProductDTO toDTO(Product product);
    @Mapping(target = "productId", ignore = true)
    public abstract Product toEntity(ProductDTO productDTO);
    @Mapping(target = "productId", ignore = true)
    public abstract Product updateProduct(Product source, @MappingTarget Product target);
}
