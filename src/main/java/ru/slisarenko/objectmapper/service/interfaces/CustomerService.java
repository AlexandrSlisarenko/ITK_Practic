package ru.slisarenko.objectmapper.service.interfaces;

import java.util.Optional;
import ru.slisarenko.objectmapper.model.entity.Customer;
import ru.slisarenko.objectmapper.service.dto.CustomerCreateDTO;
import ru.slisarenko.objectmapper.service.dto.CustomerRequestDTO;
import ru.slisarenko.objectmapper.service.dto.CustomerResponseDTO;

public interface CustomerService {
    CustomerResponseDTO getAllInformation(CustomerRequestDTO emailOrContactNumber);
    Optional<Customer> getCustomerToCreateOrder(String emailOrContactNumber);
    CustomerResponseDTO createCustomer(CustomerCreateDTO customerIn);
}
