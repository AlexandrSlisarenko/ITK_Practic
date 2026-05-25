package ru.slisarenko.persist.mapping;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.slisarenko.entity_library.dto.ShopOrderInformationStatusDTO;
import ru.slisarenko.persist.entity.ShopOrder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true)
)
public interface OrderMapping {
    @Mapping(target = "requestId", ignore = true)
    ShopOrderInformationStatusDTO toShopOrderInformationStatusDTO(ShopOrder shopOrder);
}
