package ru.slisarenko.objectmapper.service.mapper;


import java.util.List;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import ru.slisarenko.objectmapper.model.entity.Order;
import ru.slisarenko.objectmapper.model.entity.Product;
import ru.slisarenko.objectmapper.service.dto.OrderResponseDTO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true)
)
public interface OrderMapper {
    @Mapping(target = "productIds", source = "products")
    public abstract OrderResponseDTO toDto(Order order);

    default List<Long> mapProductsToIds(List<Product> products) {
        if (products == null) return null;
        return products.stream()
                .map(Product::getProductId)
                .toList();
    }
}
