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
import ru.slisarenko.jsonview.model.service.DAOCustomerService;
import ru.slisarenko.jsonview.model.service.DAOOrderService;
import ru.slisarenko.jsonview.model.service.DAOProductService;
import ru.slisarenko.jsonview.service.dto.CustomerCreateDataDTO;
import ru.slisarenko.jsonview.service.dto.CustomerInformationDTO;
import ru.slisarenko.jsonview.service.dto.CustomerInformationDetailDTO;
import ru.slisarenko.jsonview.service.dto.OrderDTO;
import ru.slisarenko.jsonview.service.dto.OrderRequestDTO;
import ru.slisarenko.jsonview.service.mapper.CustomerDataMapper;
import ru.slisarenko.jsonview.service.mapper.OrderMapper;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final DAOCustomerService customerDAO;
    private final DAOProductService productDAO;
    private final DAOOrderService orderDAO;
    private final CustomerDataMapper customerDataMapper;
    private final OrderMapper orderMapper;

    public CustomerInformationDTO createNewCustomer(CustomerCreateDataDTO request) {
        var customerRequest = this.customerDataMapper.toModelCreateCustomer(request);
        var customerFromDb = customerDAO.saveOrUpdate(customerRequest);
        return customerDataMapper.toCustomerInformationDTO(customerFromDb);
    }

    public boolean deleteCustomer(Long id) {
        return customerDAO.delete(id);
    }


    public CustomerInformationDTO updateInformCustomer(CustomerInformationDTO updateRequest) {
        var customer = customerDataMapper.toModelUpdateCustomer(updateRequest);
        return this.customerDataMapper.toCustomerInformationDTO(customerDAO.updateInfo(customer));
    }

    public Page<CustomerInformationDTO> getInformationAllCustomers(int page, int size) {
        var pageable = PageRequest.of(page, size);
        var information = customerDAO.getAllInformation(pageable).stream()
                .map(customerDataMapper::toCustomerInformationDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(information, pageable, information.size());
    }

    public CustomerInformationDetailDTO getCustomerById(Long id) {
            var customerFromDB = this.customerDAO.findEntityWithFullInformationById(id)
                    .orElseThrow(() -> new CustomerNotFoundException(id));
            var ordersFromDB = this.orderDAO.getOrderItems(id);
            return customerDataMapper.toCustomerInformationDetailDTO(customerFromDB, ordersFromDB);
    }

    public OrderDTO addOrderForCustomer(OrderRequestDTO requestOrder) {
        var customer = this.customerDAO.findCustomerById(requestOrder.customerId())
                .orElseThrow(() -> new CustomerNotFoundException(requestOrder.customerId()));
        var products = requestOrder.productsId().stream().map(productId -> this.productDAO.findProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId))).toList();
        var newOrder = Order.builder()
                .status(StatusOrder.CREATED)
                .build();
        products.forEach(newOrder::addProduct);
        customer.addOrder(newOrder);
        customer = this.customerDAO.saveOrUpdate(customer);
        var count_orders = customer.getOrders().size() - 1;
        return this.orderMapper.orderToOrderDto(customer.getOrders().get(count_orders));
    }
}
