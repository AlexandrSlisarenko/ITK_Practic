package ru.slisarenko.jsonview.service.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.slisarenko.jsonview.model.entity.Order;
import ru.slisarenko.jsonview.service.dto.OrderDTO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true)
)
public interface OrderMapper {
    OrderDTO orderToOrderDto(Order order);
}
