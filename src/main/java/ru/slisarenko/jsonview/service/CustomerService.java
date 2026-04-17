package ru.slisarenko.jsonview.service;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.slisarenko.jsonview.exceptions.CustomerNotFoundException;
import ru.slisarenko.jsonview.model.entity.Customer;
import ru.slisarenko.jsonview.model.entity.Order;
import ru.slisarenko.jsonview.model.enums.StatusOrder;
import ru.slisarenko.jsonview.model.service.DAOService;
import ru.slisarenko.jsonview.service.dto.CustomerInformationDTO;
import ru.slisarenko.jsonview.service.dto.OrderRequestDTO;
import ru.slisarenko.jsonview.service.mapper.CustomerDataMapper;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final DAOService<Customer> customerDAOService;
    private final CustomerDataMapper customerDataMapper;

    public Customer createOrderForNewCustomer(Customer request) {

        return customerDAOService.saveOrUpdate(request);
    }

    public CustomerInformationDTO createNewCustomer(CustomerInformationDTO request) {
        var customerRequest = this.customerDataMapper.toModelCreateCustomer(request);
        var customerFromDb = customerDAOService.saveOrUpdate(customerRequest);
        return customerDataMapper.toCustomerInformationDTO(customerFromDb);
    }

    public boolean deleteCustomer(Long id) {
        return customerDAOService.delete(id);
    }


    public CustomerInformationDTO updateInformCustomer(CustomerInformationDTO updateRequest) {
        var customer = customerDataMapper.toModelUpdateCustomer(updateRequest);
        return this.customerDataMapper.toCustomerInformationUpdate(customerDAOService.updateInfo(customer));
    }

    public Page<CustomerInformationDTO> getInformationAllCustomers(int page, int size) {
        var pageable = PageRequest.of(page, size);
        var information = customerDAOService.getAllInformation().stream()
                .map(customerDataMapper::toCustomerInformationDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(information, pageable, information.size());

    }

    public CustomerInformationDTO getCustomerById(Long id) {
        var result = checkId(id);
        return customerDataMapper.toCustomerInformationDTO(result);
    }

    public CustomerInformationDTO addOrderFromCustomer(OrderRequestDTO requestOrder) {
        var customer = this.customerDAOService.findById(requestOrder.customerId())
                .orElseThrow(() -> new CustomerNotFoundException(requestOrder.customerId()));
        var newOrder = Order.builder()
                .status(StatusOrder.CREATED)
                .customer(customer)
                .build();
        requestOrder.products().stream().map(this.customerDataMapper::toModelProduct).forEach(newOrder::addProduct);
        customer.addOrder(newOrder);
        return this.customerDataMapper.toCustomerInformationDTO(customer);
    }

    public Customer checkId(Long id) {
        return this.customerDAOService.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }
}
