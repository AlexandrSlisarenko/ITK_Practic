package ru.slisarenko.objectmapper.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.slisarenko.objectmapper.config.JsonQueryTestStringStatic.CUSTOMER_CREATE_ERROR_JSON;
import static ru.slisarenko.objectmapper.config.JsonQueryTestStringStatic.CUSTOMER_CREATE_JSON;
import static ru.slisarenko.objectmapper.config.JsonQueryTestStringStatic.CUSTOMER_INFORMATION_JSON;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @Test
    void createCustomerTest() {
        try {
            mockMvc.perform(post("/api/customer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(CUSTOMER_CREATE_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.firstName").value("testCustomer"))
                    .andExpect(jsonPath("$.lastName").value("testCustomer"))
                    .andExpect(jsonPath("$.email").value("testCustomer@mail.ru"))
                    .andExpect(jsonPath("$.contactNumber").value("+7(951)555-55-55"))
                    .andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getInformationCustomerTest() {
        try {
            mockMvc.perform(post("/api/customer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(CUSTOMER_CREATE_JSON));
            mockMvc.perform(get("/api/customer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(CUSTOMER_INFORMATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstName").value("testCustomer"))
                    .andExpect(jsonPath("$.lastName").value("testCustomer"))
                    .andExpect(jsonPath("$.email").value("testCustomer@mail.ru"))
                    .andExpect(jsonPath("$.contactNumber").value("+7(951)555-55-55"))
                    .andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createCustomerTest_ReturnThrowNotNull() {
        try {
            mockMvc.perform(post("/api/customer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(CUSTOMER_CREATE_ERROR_JSON))
             .andExpect(status().isBadRequest())
                    .andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}