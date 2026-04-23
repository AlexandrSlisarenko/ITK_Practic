package ru.slisarenko.objectmapper.service.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.slisarenko.objectmapper.model.entity.Customer;
import ru.slisarenko.objectmapper.model.entity.Order;
import ru.slisarenko.objectmapper.model.repository.CustomerRepository;
import ru.slisarenko.objectmapper.service.dto.CustomerCreateDTO;
import ru.slisarenko.objectmapper.service.exception.NotFoundCustomer;
import ru.slisarenko.objectmapper.service.interfaces.CustomerService;
import ru.slisarenko.objectmapper.service.mapper.CustomerMapper;
import ru.slisarenko.objectmapper.service.mapper.MapperJson;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final MapperJson jsonMapper;

    @Override
    public String getCustomerInformation(String jsonRequest) throws JsonProcessingException {
        var requestDTO = this.jsonMapper.deserializeCustomerRequestDto(jsonRequest);
        var customer = this.customerRepository.findByEmailOrContactNumber(requestDTO.email(), requestDTO.contactNumber())
                .orElseThrow(() -> new NotFoundCustomer(requestDTO.email() + " or " + requestDTO.contactNumber()));
        var responseDTO = this.customerMapper.customerToCustomerResponseDTO(customer);
        return this.jsonMapper.serializeCustomerResponseDto(responseDTO);
    }

    @Override
    public Optional<Customer> getCustomerToCreateOrder(String emailOrContactNumber) {
        return this.customerRepository.findByEmailOrContactNumber(emailOrContactNumber, emailOrContactNumber);
    }

    @Override
    public String createCustomer(String jsonRequest) throws JsonProcessingException {
        var customerIn = this.jsonMapper.deserializeCustomerCreateDto(jsonRequest);
        var customer = customerMapper.customerDTOToCustomer(customerIn);
        customer = this.customerRepository.save(customer);
        var jsonResponse = this.customerMapper.customerToCustomerResponseDTO(customer);
        return this.jsonMapper.serializeCustomerResponseDto(jsonResponse);
    }
}
