package ru.slisarenko.objectmapper.service.mapper;


import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.slisarenko.objectmapper.model.entity.Order;
import ru.slisarenko.objectmapper.service.dto.OrderResponseDTO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true)
)
public interface OrderMapper {
    public abstract OrderResponseDTO toDto(Order order);
}
