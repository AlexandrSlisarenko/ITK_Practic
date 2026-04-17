package ru.slisarenko.jsonview.controller;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.slisarenko.jsonview.config.CreatorCustomerFullData;
import ru.slisarenko.jsonview.model.enums.StatusOrder;
import ru.slisarenko.jsonview.service.CustomerService;
import ru.slisarenko.jsonview.service.dto.OrderDTO;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @MockitoBean
    private CustomerService customerService;



    @TestConfiguration
    static class TestConfig {
        @Bean
        public CreatorCustomerFullData customerData() {
            return new CreatorCustomerFullData();
        }
    }
    @Autowired
    private CreatorCustomerFullData customerData;

    @Test
    void createCustomerTest() throws Exception {
        var request = customerData.getCreateRequestDTO(null,"test_empty", "test_empty@mail.ru");
        var requestCustomer = customerData.createEmptyCustomer(null, "test_empty", "test_empty@mail.ru");
        var responseCustomer = customerData.getCreateRequestDTO(1L, "test_empty", "test_empty@mail.ru");
        when(customerService.createNewCustomer(request)).thenReturn(responseCustomer);

        mockMvc.perform(post("/api/customers/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(responseCustomer.id()))
                .andExpect(jsonPath("name").value(responseCustomer.name()))
                .andExpect(jsonPath("email").value(responseCustomer.email()));
    }

    @Test
    void informationCustomerTest() throws Exception {
        var testCustomer = customerData.getCreateRequestDTO(1L,"qwer", "asdf@dff.ru");
        var pageable = PageRequest.of(0, 1);
        var information = List.of(testCustomer);
        var page = new PageImpl<>(information, pageable, information.size());

        when(customerService.getInformationAllCustomers(0, 1)).thenReturn(page);

        mockMvc.perform(get("/api/customers?page=0&size=1"))
                .andExpect(status().isOk());
    }

    @Test
    void getCustomerDetailsTest() throws Exception {
        var order = OrderDTO.builder().status(StatusOrder.CREATED).totalPrice(BigDecimal.ONE).build();
        var testCustomer = customerData.getCreateRequestDTO(1L,"qwer", "asdf@dff.ru");
        testCustomer.orders().add(order);
        when(this.customerService.getCustomerById(1L)).thenReturn(testCustomer);
        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(testCustomer.name()))
                .andExpect(jsonPath("email").value(testCustomer.email()))
                .andExpect(jsonPath("orders").isNotEmpty())
                .andExpect(jsonPath("orders[0].totalPrice").value(testCustomer.orders().get(0).totalPrice()));
    }


}