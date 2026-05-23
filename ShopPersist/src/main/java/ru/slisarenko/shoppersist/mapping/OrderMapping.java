package ru.slisarenko.shoppersist.mapping;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.slisarenko.entity_lobrary.dto.ShopOrderInformationStatusDTO;
import ru.slisarenko.entity_lobrary.dto.order.OrderRequestDTO;
import ru.slisarenko.shoppersist.entity.ShopOrder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true)
)
public interface OrderMapping {
    @Mapping(target = "orderId" , ignore = true)
    @Mapping(target = "orderStatus" , ignore = true)
    ShopOrder toShopOrder(OrderRequestDTO dto);

    @Mapping(target = "products" , ignore = true)
    @Mapping(target = "customer" , ignore = true)
    @Mapping(target = "id" , ignore = true)
    ShopOrderInformationStatusDTO toShopOrderInformationStatusDTO(ShopOrder shopOrder);
}
