package ru.slisarenko.jsonview.config;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.slisarenko.jsonview.model.entity.Customer;
import ru.slisarenko.jsonview.model.entity.Order;
import ru.slisarenko.jsonview.model.entity.Product;
import ru.slisarenko.jsonview.model.enums.StatusOrder;
import ru.slisarenko.jsonview.model.service.DAOCustomerService;
import ru.slisarenko.jsonview.model.service.DAOProductService;
import ru.slisarenko.jsonview.service.dto.CustomerCreateDataDTO;
import ru.slisarenko.jsonview.service.dto.CustomerInformationDTO;
import ru.slisarenko.jsonview.service.dto.OrderRequestDTO;
import ru.slisarenko.jsonview.service.dto.ProductDTO;

@Component
public class CreatorData {
    @Autowired
    private DAOCustomerService customerDAO;

    @Autowired
    private DAOProductService productDAO;

    private Random random = new Random();



   /* public Customer getNewTestCustomerFromDB(){
        return serviceDAO.saveOrUpdate(createCustomer());
    }*/


    public Customer createCustomer() {
       var customer = Customer.builder()
                .email("test_email@test.ru")
                .name("test_name")
                .build();
       return this.customerDAO.saveOrUpdate(customer);
    }

    private Order createOrder() {
        return Order.builder()
                .status(StatusOrder.CREATED)
                .build();
    }

    private Product createProduct(int price) {
        var product = Product.builder()
                .name("Coffee with milk")
                .price(BigDecimal.valueOf(price))
                .build();
        return this.productDAO.saveOrUpdate(product);
    }

    public Long getNewProductId() {
        var product = Product.builder()
                .name("Coffee with milk")
                .price(BigDecimal.valueOf(10L))
                .build();
        return this.productDAO.saveOrUpdate(product).getId();
    }



    public CustomerCreateDataDTO getCreateRequestDTO(String name, String email) {
        return  CustomerCreateDataDTO.builder()
                .email(email)
                .name(name)
                .build();
    }

    public CustomerInformationDTO getCustomerInformationDTO(Long id, String name, String email) {
        return  CustomerInformationDTO.builder()
                .id(id)
                .email(email)
                .name(name)
                .build();
    }


    public ProductDTO getProductDTO(String name, BigDecimal price) {
        return ProductDTO.builder()
                .name(name)
                .price(price)
                .build();
    }

    public OrderRequestDTO getOrderRequestDTO(Long customerId, List<Long> productsId) {
        return OrderRequestDTO.builder()
                .customerId(customerId)
                .productsId(productsId)
                .build();
    }

}
