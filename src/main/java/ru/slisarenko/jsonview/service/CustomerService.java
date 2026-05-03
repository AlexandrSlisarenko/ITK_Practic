package ru.slisarenko.jsonview.service;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.slisarenko.jsonview.exceptions.CustomerNotFoundException;
import ru.slisarenko.jsonview.exceptions.ProductNotFoundException;
import ru.slisarenko.jsonview.model.entity.Order;
import ru.slisarenko.jsonview.model.enums.StatusOrder;
import ru.slisarenko.jsonview.model.service.DAOService;
import ru.slisarenko.jsonview.service.dto.CustomerCreateDataDTO;
import ru.slisarenko.jsonview.service.dto.CustomerInformationDTO;
import ru.slisarenko.jsonview.service.dto.CustomerInformationDetailDTO;
import ru.slisarenko.jsonview.service.dto.OrderRequestDTO;
import ru.slisarenko.jsonview.service.mapper.CustomerDataMapper;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final DAOService serviceDAO;
    private final CustomerDataMapper customerDataMapper;

    public CustomerInformationDTO createNewCustomer(CustomerCreateDataDTO request) {
        var customerRequest = this.customerDataMapper.toModelCreateCustomer(request);
        var customerFromDb = serviceDAO.saveOrUpdate(customerRequest);
        return customerDataMapper.toCustomerInformationDTO(customerFromDb);
    }

    public boolean deleteCustomer(Long id) {
        return serviceDAO.delete(id);
    }


    public CustomerInformationDTO updateInformCustomer(CustomerInformationDTO updateRequest) {
        var customer = customerDataMapper.toModelUpdateCustomer(updateRequest);
        return this.customerDataMapper.toCustomerInformationDTO(serviceDAO.updateInfo(customer));
    }

    public Page<CustomerInformationDetailDTO> getInformationAllCustomers(int page, int size) {
        var pageable = PageRequest.of(page, size);
        var information = serviceDAO.getAllInformation(pageable).stream()
                .map(customerDataMapper::toCustomerInformationDetailDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(information, pageable, information.size());

    }

    public CustomerInformationDetailDTO getCustomerById(Long id) {
        if(checkId(id)) {
            var result = this.serviceDAO.findEntityWithFullInformationById(id).get();
            return customerDataMapper.toCustomerInformationDetailDTO(result);
        }
        throw new CustomerNotFoundException(id);
    }

    public CustomerInformationDetailDTO addOrderForCustomer(OrderRequestDTO requestOrder) {
        var customer = this.serviceDAO.findCustomerById(requestOrder.customerId())
                .orElseThrow(() -> new CustomerNotFoundException(requestOrder.customerId()));
        var products = requestOrder.productsId().stream().map(productId -> this.serviceDAO.findProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId))).toList();
        var newOrder = Order.builder()
                .status(StatusOrder.CREATED)
                .customer(customer)
                .build();
        products.forEach(newOrder::addProduct);
        customer.addOrder(newOrder);
        customer = this.serviceDAO.saveOrUpdate(customer);
        return this.customerDataMapper.toCustomerInformationDetailDTO(customer);
    }

    public boolean checkId(Long id) {
        return this.serviceDAO.existsCustomerById(id);
    }
}
