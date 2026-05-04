package ru.slisarenko.jsonview.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.slisarenko.jsonview.config.CreatorData;
import ru.slisarenko.jsonview.model.enums.StatusOrder;
import ru.slisarenko.jsonview.service.dto.CustomerInformationDTO;
import ru.slisarenko.jsonview.service.dto.CustomerInformationDetailDTO;
import ru.slisarenko.jsonview.service.dto.ProductDTO;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class CustomerControllerTest {

    private static final String CUSTOMER_NAME = "test_empty";
    private static final String CUSTOMER_EMAIL = "test_empty@mail.ru";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public CreatorData creatorData() {
            return new CreatorData();
        }
    }

    @Autowired
    private CreatorData creatorData;

    @Test
    void createCustomerTest() throws Exception {
        var request = creatorData.getCreateRequestDTO(CUSTOMER_NAME, CUSTOMER_EMAIL);
        var createdCustomerDTO = creatorData.getCustomerInformationDTO(1L, CUSTOMER_NAME, CUSTOMER_EMAIL);

        mockMvc.perform(post("/api/customers/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("name").value(createdCustomerDTO.name()))
                .andExpect(jsonPath("email").value(createdCustomerDTO.email()));
    }

    @Test
    void addOrderFromCustomers() throws Exception {

        var requestCustomer = creatorData.getCreateRequestDTO(CUSTOMER_NAME, CUSTOMER_EMAIL);
        var jsonCustomer = mockMvc.perform(post("/api/customers/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestCustomer))
                )
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        var customer = objectMapper.readValue(jsonCustomer, CustomerInformationDTO.class);

        var products = Arrays.asList(
                creatorData.getProductDTO("Coffee", BigDecimal.valueOf(35.33)),
                creatorData.getProductDTO("Milk", BigDecimal.valueOf(15.33)),
                creatorData.getProductDTO("Сocoa", BigDecimal.valueOf(5.33)),
                creatorData.getProductDTO("Bread", BigDecimal.valueOf(15.33)),
                creatorData.getProductDTO("Pate", BigDecimal.valueOf(25.33))
        );
        products = products.stream().map(product -> {
            try {
                return objectMapper.readValue(mockMvc.perform(post("/api/product")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product))).andExpect(status().isCreated())
                        .andReturn().getResponse().getContentAsString(), ProductDTO.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }).toList();

        var test_order_request = creatorData.getOrderRequestDTO(customer.id(), List.of(products.get(0).id(),
                products.get(2).id(),
                products.get(3).id()));

        mockMvc.perform(post("/api/customers/add_order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(test_order_request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("status").value(StatusOrder.CREATED.name()))
                .andExpect(jsonPath("totalPrice").value(55.99));
    }

    @Test
    void getCustomerDetails() throws Exception {
        var requestCustomer = creatorData.getCreateRequestDTO(CUSTOMER_NAME, CUSTOMER_EMAIL);
        var jsonCustomer = mockMvc.perform(post("/api/customers/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestCustomer))
                )
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        var customerResponse = objectMapper.readValue(jsonCustomer, CustomerInformationDTO.class);

        var products = Arrays.asList(
                creatorData.getProductDTO("Coffee", BigDecimal.valueOf(35.33)),
                creatorData.getProductDTO("Milk", BigDecimal.valueOf(15.33)),
                creatorData.getProductDTO("Сocoa", BigDecimal.valueOf(5.33)),
                creatorData.getProductDTO("Bread", BigDecimal.valueOf(15.33)),
                creatorData.getProductDTO("Pate", BigDecimal.valueOf(25.33))
        );
        products = products.stream().map(product -> {
            try {
                return objectMapper.readValue(mockMvc.perform(post("/api/product")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product))).andExpect(status().isCreated())
                        .andReturn().getResponse().getContentAsString(), ProductDTO.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }).toList();

        var test_order_request = creatorData.getOrderRequestDTO(customerResponse.id(), List.of(products.get(0).id(),
                products.get(1).id(),
                products.get(2).id()));
        mockMvc.perform(post("/api/customers/add_order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(test_order_request))
        );
        test_order_request = creatorData.getOrderRequestDTO(customerResponse.id(), List.of(products.get(3).id(),
                products.get(4).id()));
        mockMvc.perform(post("/api/customers/add_order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(test_order_request))
        );

        var jsonResponse = mockMvc.perform(get("/api/customers/" + customerResponse.id())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        var customer = objectMapper.readValue(jsonResponse, CustomerInformationDetailDTO.class);

        Assertions.assertNotNull(customer);
        Assertions.assertEquals(customerResponse.id(), customer.id());
        Assertions.assertEquals(customerResponse.name(), customer.name());
        Assertions.assertEquals(customerResponse.email(), customer.email());
        Assertions.assertEquals(2, customer.orders().size());
        Assertions.assertEquals(BigDecimal.valueOf(40.66), customer.orders().get(1).totalPrice());
    }
}