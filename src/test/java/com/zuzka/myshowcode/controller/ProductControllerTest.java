package com.zuzka.myshowcode.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzka.myshowcode.dto.ProductRequest;
import com.zuzka.myshowcode.entity.Product;
import com.zuzka.myshowcode.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void addNewProduct() throws Exception {
        ProductRequest product = new ProductRequest("rohlik", 50, 1.50);
        mockMvc.perform(post("/products")
                .content(objectMapper.writeValueAsString(product))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\":\"SUCCESS\",\"message\":\"Product added\"}"));
    }

    @Test
    void updateProductById() throws Exception {
        ProductRequest product = new ProductRequest("rohlik", 100, 1.90);
        mockMvc.perform(put("/products")
                .content(objectMapper.writeValueAsString(product))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\":\"SUCCESS\",\"message\":\"Product updated\"}"));
    }

    @Test
    void getAllProducts() throws Exception {
        Product product = new Product((long) 1, "rohlik", 50, 1.50);
        List<Product> products = Arrays.asList(product);

        Mockito.when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\":\"SUCCESS\",\"message\":\"[{\\\"id\\\":1,\\\"name\\\":\\\"rohlik\\\",\\\"quantityInStock\\\":50,\\\"pricePerUnit\\\":1.5}]\"}"));
    }

    @Test
    void getProductById() throws Exception {
        Product product = new Product((long) 123, "rohlik", 50, 1.50);

        Mockito.when(productService.getProductById(Mockito.anyLong())).thenReturn(Optional.of(product));

        mockMvc.perform(get("/products/123"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\":\"SUCCESS\",\"message\":\"{\\\"id\\\":123,\\\"name\\\":\\\"rohlik\\\",\\\"quantityInStock\\\":50,\\\"pricePerUnit\\\":1.5}\"}"));
    }

    @Test
    void getProductByName() throws Exception {
        Product product = new Product((long) 123, "rohlik", 50, 1.50);

        Mockito.when(productService.getProductByName(Mockito.anyString())).thenReturn(Optional.of(product));

        mockMvc.perform(get("/products/getByName").param("name", "rohlik"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"status\":\"SUCCESS\",\"message\":\"{\\\"id\\\":123,\\\"name\\\":\\\"rohlik\\\",\\\"quantityInStock\\\":50,\\\"pricePerUnit\\\":1.5}\"}"));
    }

    @Test
    void deleteProductById() throws Exception {
        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"status\":\"SUCCESS\",\"message\":\"Product deleted\"}"));
    }
}