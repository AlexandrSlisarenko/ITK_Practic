package ru.slisarenko.jsonview.service;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.slisarenko.jsonview.config.CreatorData;
import ru.slisarenko.jsonview.service.dto.CustomerInformationDTO;

@SpringBootTest
class CustomerServiceTest {
    private static final String CUSTOMER_NAME = "test_empty";
    private static final String CUSTOMER_EMAIL = "test_empty@mail.ru";

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CreatorData creatorData;

    @Test
    void createEmptyCustomerTest() {
        var name = "test_empty";
        var email = "test_empty@mail.ru";
        var response = getTestCustomer();
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertEquals(name, response.name());
        Assertions.assertEquals(email, response.email());
    }

    @Test
    void addOrderCustomerTest(){
        var testCustomer = getTestCustomer();
        var listProduct = List.of(creatorData.getProductDTO("хлеб", BigDecimal.valueOf(50L)),
                creatorData.getProductDTO("молоко", BigDecimal.valueOf(100L)));
        var requestOrder = creatorData.getOrderRequestDTO(testCustomer.id(), listProduct);
        CustomerInformationDTO response = this.customerService.addOrderFromCustomer(requestOrder);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(testCustomer.id(), response.id());
        Assertions.assertFalse(response.orders().isEmpty());
        Assertions.assertEquals(150, response.orders().get(0).totalPrice().intValue());
    }

    @Test
    void deleteCustomerTest() {
        var testCustomer = creatorData.getNewTestCustomerFromDB();
        Assertions.assertTrue(customerService.deleteCustomer(testCustomer.getId()));
    }

    @Test
    void updateCustomerTest() {
        var testStr = "Upated Customer";
        var testCustomer = creatorData.getNewTestCustomerFromDB();
        testCustomer.setName(testStr);
        var data = CustomerInformationDTO.builder().id(testCustomer.getId()).name(testStr).email("new@mail.ru").build();
        var testCustomerNew = this.customerService.updateInformCustomer(data);
        Assertions.assertNotNull(testCustomerNew);
        Assertions.assertEquals(testStr, testCustomerNew.name());
    }

    @Test
    void getAllCustomerInformationTest(){
        /*creatorData.createCustomers(2);
        var countCustomer = this.customerService.getAllCustomers(0,1).size();
        Page<Customer> customersInformation = this.customerService.getInformationAllCustomers(0,100);
        var testCustomer = customersInformation.getContent();
        //Assertions.assertEquals(countCustomer, testCustomer.size());
        Assertions.assertNotNull(testCustomer.get(0).getId());
        Assertions.assertNotNull(testCustomer.get(0).getName());
        Assertions.assertNotNull(testCustomer.get(0).getEmail());*/
    }

    /*@Test
    void getCustomerByIdTest() {
        creatorData.getNewTestCustomerFromDB();
        var id = this.customerService.getInformationAllCustomers(0,100).getContent().get(0).getId();
        Customer testCustomer = this.customerService.getCustomerById(id);
        Assertions.assertNotNull(testCustomer);
        Assertions.assertEquals(id, testCustomer.getId());
        //Assertions.assertFalse(testCustomer.getOrders().isEmpty());
        //Assertions.assertFalse(testCustomer.getOrders().get(0).getProducts().isEmpty());
    }*/

    private CustomerInformationDTO getTestCustomer() {
        var request = creatorData.getCreateRequestDTO(CUSTOMER_NAME, CUSTOMER_EMAIL);
        return customerService.createNewCustomer(request);
    }

}