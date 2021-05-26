package com.zuzka.myshowcode.controller;

import com.zuzka.myshowcode.entity.Product;
import com.zuzka.myshowcode.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @Test
    void addNewProduct() {
    }

    @Test
    void updateProductById() {
    }

    @Test
    void getAllProducts() throws Exception {
        Product product = new Product((long) 1, "rohlik", 50, 1.50);
        List<Product> products = Arrays.asList(product);

        Mockito.when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"status\":\"SUCCESS\",\"message\":\"[Product(id=1, name=rohlik, quantityInStock=50, pricePerUnit=1.5)]\"}"));
    }

    @Test
    void getProductById() {
    }

    @Test
    void getProductByName() {
    }
}