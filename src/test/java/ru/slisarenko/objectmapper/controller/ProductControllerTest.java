package ru.slisarenko.objectmapper.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.slisarenko.objectmapper.config.JsonQueryTestStringStatic.PRODUCT_CREATE_ERROR_PRICE_NULL_QUANTITY_0_JSON;
import static ru.slisarenko.objectmapper.config.JsonQueryTestStringStatic.PRODUCT_CREATE_JSON;
import static ru.slisarenko.objectmapper.config.JsonQueryTestStringStatic.PRODUCT_UPDATE_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllProductsTest() {
        try {
            for (int i = 0; i < 5; i++) {
                mockMvc.perform(post("/api/products")
                        .content(PRODUCT_CREATE_JSON)
                        .contentType(MediaType.APPLICATION_JSON));
            }
            var result = mockMvc.perform(get("/api/products?page=1&size=2")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk()).andReturn();
            System.out.println(result.getResponse().getContentAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getProductTest() {
        try {
            var result = mockMvc.perform(post("/api/products")
                    .content(PRODUCT_CREATE_JSON)
                    .contentType(MediaType.APPLICATION_JSON)).andReturn();
            var data = result.getResponse().getContentAsString();
            var root = objectMapper.readTree(data);

            mockMvc.perform(get("/api/products/" + root.get("productId").asString())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("testProduct"))
                    .andExpect(jsonPath("$.description").value("testProduct"))
                    .andExpect(jsonPath("$.price").value(123.54))
                    .andExpect(jsonPath("$.quantityInStock").value(10));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void createProductTest_ReturnsCreatedProduct() {
        try {
            mockMvc.perform(post("/api/products")
                            .content(PRODUCT_CREATE_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value("testProduct"))
                    .andExpect(jsonPath("$.description").value("testProduct"))
                    .andExpect(jsonPath("$.price").value(123.54))
                    .andExpect(jsonPath("$.quantityInStock").value(10));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void createProductTest_ReturnsExceptionBadRequest() {
        try {
            mockMvc.perform(post("/api/products")
                            .content(PRODUCT_CREATE_ERROR_PRICE_NULL_QUANTITY_0_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void updateProduct() {
        try {
            var result = mockMvc.perform(post("/api/products")
                    .content(PRODUCT_CREATE_JSON)
                    .contentType(MediaType.APPLICATION_JSON)).andReturn();
            var data = result.getResponse().getContentAsString();
            var root = objectMapper.readTree(data);
            mockMvc.perform(put("/api/products/" + root.get("productId").asString())
                    .content(PRODUCT_UPDATE_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("testProductUpdate"))
                    .andExpect(jsonPath("$.description").value("testProductUpdate"))
                    .andExpect(jsonPath("$.price").value(123.54))
                    .andExpect(jsonPath("$.quantityInStock").value(145));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteProduct() {
        try {
            var result = mockMvc.perform(post("/api/products")
                    .content(PRODUCT_CREATE_JSON)
                    .contentType(MediaType.APPLICATION_JSON)).andReturn();
            var data = result.getResponse().getContentAsString();
            var root = objectMapper.readTree(data);
            mockMvc.perform(delete("/api/products/" + root.get("productId").asString()))
                    .andExpect(status().isNoContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}