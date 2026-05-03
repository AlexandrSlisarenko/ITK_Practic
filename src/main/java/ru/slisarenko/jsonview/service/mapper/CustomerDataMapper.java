package ru.slisarenko.jsonview.service.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.slisarenko.jsonview.model.entity.Customer;
import ru.slisarenko.jsonview.model.entity.Product;
import ru.slisarenko.jsonview.service.dto.CustomerCreateDataDTO;
import ru.slisarenko.jsonview.service.dto.CustomerInformationDTO;
import ru.slisarenko.jsonview.service.dto.CustomerInformationDetailDTO;
import ru.slisarenko.jsonview.service.dto.ProductDTO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true),
        uses = OrderMapper.class
)
public interface CustomerDataMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orders", ignore = true)
    public abstract Customer toModelCreateCustomer(CustomerCreateDataDTO createCustomerDto);

    @Mapping(target = "orders", ignore = true)
    public abstract Customer toModelUpdateCustomer(CustomerInformationDTO updateCustomerDto);

    public abstract CustomerInformationDetailDTO toCustomerInformationDetailDTO(Customer customer);

    public abstract CustomerInformationDTO toCustomerInformationDTO(Customer customer);

    @Mapping(target = "id", ignore = true)
    public abstract Product toModelProduct(ProductDTO productDto);


}
