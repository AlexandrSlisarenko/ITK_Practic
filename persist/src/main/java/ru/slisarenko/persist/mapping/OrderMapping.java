package ru.slisarenko.persist.mapping;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import ru.slisarenko.entity_library.dto.ShopOrderInformationStatusDTO;
import ru.slisarenko.entity_library.dto.persist.PersistDTO;
import ru.slisarenko.persist.entity.ShopOrder;
import ru.slisarenko.persist.entity.ShopProduct;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true)
)
public interface OrderMapping {
    @Named("toId")
    default Long productToId(ShopProduct product) {
        return product != null ? product.getProductId() : null;
    }

    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "requestUUId", source = "orderUUID")
    ShopOrderInformationStatusDTO toShopOrderInformationStatusDTO(ShopOrder shopOrder);

    @Mapping(target = "requestUUId" , source = "orderUUID")
    @Mapping(target = "customerId", source = "customer.customerId")
    @Mapping(target = "productIds", source = "products", qualifiedByName = "toId")
    PersistDTO toPersistDTO(ShopOrder shopOrder);


}
