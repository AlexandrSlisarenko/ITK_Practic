package ru.slisarenko.objectmapper.service.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.slisarenko.objectmapper.model.entity.Product;
import ru.slisarenko.objectmapper.service.dto.ProductDTO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true)
)
public interface ProductMapper {
    public abstract ProductDTO toDTO(Product product);
    public abstract Product toEntity(ProductDTO productDTO);
    public abstract Product updateProduct(Product source, @MappingTarget Product target);
}
