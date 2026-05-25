package ru.slisarenko.persist.mapping;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.slisarenko.entity_library.dto.ShopOrderInformationStatusDTO;
import ru.slisarenko.entity_library.dto.persist.PersistDTO;
import ru.slisarenko.persist.entity.ShopOrder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true)
)
public interface OrderMapping {
    @Mapping(target = "requestId", ignore = true)
    @Mapping(target = "requestUUId", source = "orderUUID")
    ShopOrderInformationStatusDTO toShopOrderInformationStatusDTO(ShopOrder shopOrder);

    @Mapping(target = "requestUUId" , source = "orderUUID")
    @Mapping(target = "customerId", source = "customer.customerId")
    @Mapping(target = "productIds", source = "products")
    PersistDTO toPersistDTO(ShopOrder shopOrder);


}
