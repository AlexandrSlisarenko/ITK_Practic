package ru.slisarenko.jsonview.config;

import java.util.ArrayList;
import org.springframework.stereotype.Component;
import ru.slisarenko.jsonview.model.entity.Customer;
import ru.slisarenko.jsonview.service.dto.CustomerInformationDTO;

@Component
public class CreatorCustomerFullData {

    public Customer createEmptyCustomer(Long id,String name, String email) {
        return Customer.builder()
                .id(id)
                .email(email)
                .name(name)
                .build();
    }




    public CustomerInformationDTO getCreateRequestDTO(Long id,String name, String email) {
        return  CustomerInformationDTO.builder()
                .id(id)
                .orders(new ArrayList<>())
                .email(email)
                .name(name)
                .build();
    }
}
