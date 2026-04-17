package ru.slisarenko.jsonview.service.mapper;

import java.util.List;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.slisarenko.jsonview.model.entity.Order;
import ru.slisarenko.jsonview.service.dto.OrderDTO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true)
)
public interface OrderMapper {

    List<OrderDTO> ordersToOrderDTOs(List<Order> orders);

    OrderDTO orderToOrderDto(Order order);
}
