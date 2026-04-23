package ru.slisarenko.objectmapper.service.mapper;

import java.util.List;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.slisarenko.objectmapper.model.entity.Customer;
import ru.slisarenko.objectmapper.model.entity.Order;
import ru.slisarenko.objectmapper.model.entity.Product;
import ru.slisarenko.objectmapper.service.dto.CustomerCreateDTO;
import ru.slisarenko.objectmapper.service.dto.CustomerResponseDTO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true)
)
public interface CustomerMapper {
    @Mapping(target = "orders", source = "orders")
    public abstract CustomerResponseDTO customerToCustomerResponseDTO(Customer customer);
    @Mapping(target = "customerId", ignore = true)
    @Mapping(target = "orders", ignore = true)
    public abstract Customer customerDTOToCustomer(CustomerCreateDTO customerCreateDTO);

    default List<Long> mapOrdersToIds(List<Order> products) {
        if (products == null) return null;
        return products.stream()
                .map(Order::getOrderId)
                .toList();
    }
}
