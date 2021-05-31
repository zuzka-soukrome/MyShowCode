package com.zuzka.myshowcode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzka.myshowcode.controller.OrderController;
import com.zuzka.myshowcode.controller.ProductController;
import com.zuzka.myshowcode.dto.ProductRequest;
import com.zuzka.myshowcode.entity.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:integration-test.properties")
class MyShowCodeApplicationIT {

    @Autowired
    ProductController productController;

    @Autowired
    OrderController orderController;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
        Assertions.assertThat(productController).isNotNull();
        Assertions.assertThat(orderController).isNotNull();
    }

    @Test
    public void testProductCreateUpdateFindDelete() throws JsonProcessingException {
        ProductRequest product = new ProductRequest("rohlik", 100, 1.90);
        productController.addNewProduct(product);

        List<Product> products = jsonToList(productController.getAllProducts().getBody().getMessage());
        Assertions.assertThat(products).first().hasFieldOrPropertyWithValue("name", "rohlik");

        ProductRequest productUpdate = new ProductRequest("rohlik", 150, 1.90);
        productController.updateProduct(productUpdate);

        List<Product> productsUpdated = jsonToList(productController.getAllProducts().getBody().getMessage());
        Assertions.assertThat(productsUpdated).first().hasFieldOrPropertyWithValue("quantityInStock", 150);

        productController.deleteProductById((long) 1);
        List<Product> productsDeleted = jsonToList(productController.getAllProducts().getBody().getMessage());
        Assertions.assertThat(productsDeleted).isEmpty();
    }

    private List<Product> jsonToList(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, new TypeReference<List<Product>>() {
        });
    }


}
