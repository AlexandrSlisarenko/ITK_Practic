package ru.slisarenko.objectmapper.service.implementations;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.slisarenko.objectmapper.model.entity.Customer;
import ru.slisarenko.objectmapper.model.repository.CustomerRepository;
import ru.slisarenko.objectmapper.service.dto.CustomerCreateDTO;
import ru.slisarenko.objectmapper.service.dto.CustomerRequestDTO;
import ru.slisarenko.objectmapper.service.dto.CustomerResponseDTO;
import ru.slisarenko.objectmapper.service.exception.NotFoundCustomer;
import ru.slisarenko.objectmapper.service.interfaces.CustomerService;
import ru.slisarenko.objectmapper.service.mapper.CustomerMapper;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerResponseDTO getAllInformation(CustomerRequestDTO requestDTO) {
        var customer = this.customerRepository.findByEmailOrContactNumber(requestDTO.email(), requestDTO.contactNumber())
                .orElseThrow(() -> new NotFoundCustomer(requestDTO.email() + " or " + requestDTO.contactNumber()));
        return this.customerMapper.customerToCustomerResponseDTO(customer);
    }

    @Override
    public Optional<Customer> getCustomerToCreateOrder(String emailOrContactNumber) {
        return this.customerRepository.findByEmailOrContactNumber(emailOrContactNumber, emailOrContactNumber);
    }

    @Override
    public CustomerResponseDTO createCustomer(CustomerCreateDTO customerIn) {
        var customer = customerMapper.customerDTOToCustomer(customerIn);
        customer = this.customerRepository.save(customer);
        return this.customerMapper.customerToCustomerResponseDTO(customer);
    }
}
