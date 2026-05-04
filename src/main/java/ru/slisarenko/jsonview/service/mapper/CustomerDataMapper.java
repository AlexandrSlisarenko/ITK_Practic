package ru.slisarenko.jsonview.service.mapper;

import java.util.List;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.slisarenko.jsonview.model.entity.Customer;
import ru.slisarenko.jsonview.model.entity.Product;
import ru.slisarenko.jsonview.service.dto.CustomerCreateDataDTO;
import ru.slisarenko.jsonview.service.dto.CustomerInformationDTO;
import ru.slisarenko.jsonview.service.dto.CustomerInformationDetailDTO;
import ru.slisarenko.jsonview.service.dto.OrderDTO;
import ru.slisarenko.jsonview.service.dto.ProductDTO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true),
        uses = OrderMapper.class
)
public interface CustomerDataMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orders", ignore = true)
    Customer toModelCreateCustomer(CustomerCreateDataDTO createCustomerDto);

    @Mapping(target = "orders", ignore = true)
    Customer toModelUpdateCustomer(CustomerInformationDTO updateCustomerDto);

    CustomerInformationDetailDTO toCustomerInformationDetailDTO(CustomerInformationDTO customer,
                                                                                List<OrderDTO> orders);
    CustomerInformationDTO toCustomerInformationDTO(Customer customer);



}
