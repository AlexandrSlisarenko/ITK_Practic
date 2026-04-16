package ru.slisarenko.jsonview.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.slisarenko.jsonview.model.entity.Customer;
import ru.slisarenko.jsonview.model.entity.Order;
import ru.slisarenko.jsonview.model.entity.Product;
import ru.slisarenko.jsonview.model.enums.StatusOrder;

@SpringBootTest
class CustomerServiceTest {
    @Autowired
    private CustomerService customerService;

    private Random random = new Random();

    @Test
    void createCustomerTest() {

        var testCustomer = createCustomer();
        testCustomer = customerService.createOrderForNewCustomer(testCustomer);
        Assertions.assertNotNull(testCustomer);
        Assertions.assertNotNull(testCustomer.getId());
        Assertions.assertNotNull(testCustomer.getOrders().get(0).getId());
        Assertions.assertNotNull(testCustomer.getOrders().get(0).getProducts().get(0).getId());
    }

    @Test
    void deleteCustomerTest() {
        var testCustomer = getNewTestCustomerFromDB();
        Assertions.assertTrue(customerService.deleteCustomer(testCustomer));
    }

    @Test
    void updateCustomerTest() {
        var testStr = "Upated Customer";
        var testCustomer = getNewTestCustomerFromDB();
        testCustomer.setName(testStr);
        testCustomer = this.customerService.updateInformCustomer(testCustomer);
        Assertions.assertNotNull(testCustomer);
        Assertions.assertEquals(testStr, testCustomer.getName());
    }

    @Test
    void getAllCustomerInformationTest(){
        createCustomers(2);
        var countCustomer = this.customerService.getAllCustomers().size();
        List<Customer> customersInformation = this.customerService.getInformationAllCustomers();
        Assertions.assertEquals(countCustomer, customersInformation.size());
        Assertions.assertNotNull(customersInformation.get(0).getId());
        Assertions.assertNotNull(customersInformation.get(0).getName());
        Assertions.assertNotNull(customersInformation.get(0).getEmail());
    }

    @Test
    void getCustomerByIdTest() {
        getNewTestCustomerFromDB();
        this.customerService.getInformationAllCustomers().forEach(el -> System.out.println(el.getId()));
        var id = this.customerService.getInformationAllCustomers().get(0).getId();
        Customer testCustomer = this.customerService.getCustomerById(id);
        Assertions.assertNotNull(testCustomer);
        Assertions.assertEquals(id, testCustomer.getId());
        Assertions.assertFalse(testCustomer.getOrders().isEmpty());
        Assertions.assertFalse(testCustomer.getOrders().get(0).getProducts().isEmpty());
    }

    private void createCustomers(int count) {
        for (int i = 0; i < count; i++) {
            getNewTestCustomerFromDB();
        }
    }

    private Customer getNewTestCustomerFromDB(){
        return customerService.createOrderForNewCustomer(createCustomer());
    }


    private Customer createCustomer() {
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
}