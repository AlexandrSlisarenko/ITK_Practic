package ru.slisarenko.objectmapper.controller;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.slisarenko.objectmapper.config.JsonQueryTestStringStatic.CUSTOMER_CREATE_JSON;
import static ru.slisarenko.objectmapper.config.JsonQueryTestStringStatic.ORDER_CREATE_JSON;
import static ru.slisarenko.objectmapper.config.JsonQueryTestStringStatic.PRODUCT_CREATE_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createOrderTest_ReturnOrder() {
        try {
            mockMvc.perform(post("/api/customer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(CUSTOMER_CREATE_JSON));
            var productIds = new ArrayList<String>();
            for (int i = 0; i < 5; i++) {
                var root = objectMapper.readTree(mockMvc.perform(post("/api/products")
                        .content(PRODUCT_CREATE_JSON)).andReturn().getResponse().getContentAsString());
                productIds.add(root.get("productId").asString());
            }

            var ids = objectMapper.writeValueAsString(productIds);
            var json = String.format(ORDER_CREATE_JSON,ids);
            System.out.println("ORDER_CREATE_JSON");
            System.out.println(json);
            var str = objectMapper.readTree(mockMvc.perform(post("/api/orders")
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andReturn().getResponse().getContentAsString());
            System.out.println(str);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getOrder_ReturnOrder() {
        try{
            mockMvc.perform(post("/api/customer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(CUSTOMER_CREATE_JSON));
            var productIds = new ArrayList<String>();
            for (int i = 0; i < 5; i++) {
                var root = objectMapper.readTree(mockMvc.perform(post("/api/products")
                        .content(PRODUCT_CREATE_JSON)).andReturn().getResponse().getContentAsString());
                productIds.add(root.get("productId").asString());
            }

            var ids = objectMapper.writeValueAsString(productIds);
            var json = String.format(ORDER_CREATE_JSON,ids);
            System.out.println("ORDER_CREATE_JSON");
            System.out.println(json);
            var root = objectMapper.readTree(mockMvc.perform(post("/api/orders")
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse().getContentAsString());
            var str = objectMapper.readTree(mockMvc.perform(get("/api/orders/" + root.get("orderId").asString())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString());
            System.out.println("getOrder_ReturnOrder");
            System.out.println(str);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}