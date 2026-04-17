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
import ru.slisarenko.jsonview.service.CustomerService;
import ru.slisarenko.jsonview.service.dto.CustomerInformationDTO;
import ru.slisarenko.jsonview.service.dto.OrderRequestDTO;
import ru.slisarenko.jsonview.service.dto.ProductDTO;

@Component
public class CreatorData {
    @Autowired
    private CustomerService customerService;

    private Random random = new Random();



    public Customer getNewTestCustomerFromDB(){
        return customerService.createOrderForNewCustomer(createCustomer());
    }


    public Customer createCustomer() {
        var order = createOrder();
        var number = random.nextInt();
        order.addProduct(createProduct(number));
        return Customer.builder()
                .email("test" + number + "@mail.ru")
                .name("Test Customer " + number)
                .orders(List.of(order))
                .build();
    }

    private Order createOrder() {
        return Order.builder()
                .status(StatusOrder.CREATED)
                .build();
    }

    private Product createProduct(int price) {
        return Product.builder()
                .name("Coffee with milk")
                .price(BigDecimal.valueOf(price))
                .build();
    }



    public CustomerInformationDTO getCreateRequestDTO(String name, String email) {
        return  CustomerInformationDTO.builder()
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

    public OrderRequestDTO getOrderRequestDTO(Long customerId, List<ProductDTO> products) {
        return OrderRequestDTO.builder()
                .customerId(customerId)
                .products(products)
                .build();
    }

}
