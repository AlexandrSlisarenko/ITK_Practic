package ru.slisarenko.objectmapper.service.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Optional;
import ru.slisarenko.objectmapper.model.entity.Customer;
import ru.slisarenko.objectmapper.service.dto.CustomerRequestDTO;

public interface CustomerService {
    String getCustomerInformation(String emailOrContactNumber) throws JsonProcessingException;
    Optional<Customer> getCustomerToCreateOrder(String emailOrContactNumber);
    String createCustomer(String jsonCustomer) throws JsonProcessingException;
}
