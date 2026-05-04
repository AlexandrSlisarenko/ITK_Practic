package ru.slisarenko.jsonview.config;

import java.util.ArrayList;
import org.springframework.stereotype.Component;
import ru.slisarenko.jsonview.model.entity.Customer;
import ru.slisarenko.jsonview.service.dto.CustomerInformationDetailDTO;

@Component
public class CreatorCustomerFullData {

    /*public Customer createEmptyCustomer(Long id,String name, String email) {
        return Customer.builder()
                .id(id)
                .email(email)
                .name(name)
                .build();
    }




    public CustomerInformationDetailDTO getCreateRequestDTO(Long id, String name, String email) {
        return  CustomerInformationDetailDTO.builder()
                .id(id)
                .ordersId(new ArrayList<>())
                .email(email)
                .name(name)
                .build();
    }*/
}
