package com.zuzka.myshowcode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzka.myshowcode.controller.OrderController;
import com.zuzka.myshowcode.controller.ProductController;
import com.zuzka.myshowcode.dto.ItemRequest;
import com.zuzka.myshowcode.dto.OrderRequest;
import com.zuzka.myshowcode.dto.ProductRequest;
import com.zuzka.myshowcode.entity.Order;
import com.zuzka.myshowcode.entity.Product;
import com.zuzka.myshowcode.enums.OrderState;
import com.zuzka.myshowcode.service.OrderService;
import com.zuzka.myshowcode.service.ProductService;
import com.zuzka.myshowcode.service.ScheduleService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:integration-test.properties")
@TestPropertySource(properties = "scheduler.rate.ms=100")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MyShowCodeApplicationIT {

    @Autowired
    ProductController productController;

    @Autowired
    OrderController orderController;

    @SpyBean
    ProductService productService;

    @SpyBean
    OrderService orderService;

    @SpyBean
    ScheduleService scheduleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
        Assertions.assertThat(productController).isNotNull();
        Assertions.assertThat(orderController).isNotNull();
    }

    @Test
    void testScheludedTask() {
        await()
                .atMost(1, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(scheduleService, times(5)).scheduleFixedRateTask());
    }

    @Test
    void testProductCreateUpdateFindDelete() throws Exception {
        ProductRequest product = new ProductRequest("rohlik", 100, 1.90);
        productController.addNewProduct(product);

        List<Product> products = jsonToListOfProducts(productController.getAllProducts().getBody().getMessage());
        Assertions.assertThat(products).first().hasFieldOrPropertyWithValue("name", "rohlik");

        ProductRequest productUpdate = new ProductRequest("rohlik", 150, 1.90);
        productController.updateProduct(productUpdate);

        List<Product> productsUpdated = jsonToListOfProducts(productController.getAllProducts().getBody().getMessage());
        Assertions.assertThat(productsUpdated).first().hasFieldOrPropertyWithValue("quantityInStock", 150);

        productController.deleteProductById((long) 1);
        List<Product> productsDeleted = jsonToListOfProducts(productController.getAllProducts().getBody().getMessage());
        Assertions.assertThat(productsDeleted).isEmpty();
    }

    @Test
    void testCreateAndExpireOrder() throws Exception {
        ProductRequest product = new ProductRequest("rohlik", 100, 1.90);
        productController.addNewProduct(product);

        ReflectionTestUtils.setField(orderService, "orderExpirationTime", 1);

        ItemRequest orderItem = new ItemRequest("rohlik", 20);
        List<ItemRequest> orderItems = Arrays.asList(orderItem);
        OrderRequest order = new OrderRequest(orderItems);

        orderController.addNewOrder(order);
        List<Order> orders = jsonToListOfOrders(orderController.getAllOrders().getBody().getMessage());
        Assertions.assertThat(orders).isNotEmpty();
        Assertions.assertThat(orders.get(0).getState()).isEqualTo(OrderState.NEW);

        List<Product> products = jsonToListOfProducts(productController.getAllProducts().getBody().getMessage());
        Assertions.assertThat(products.get(0).getQuantityInStock()).isEqualTo(80);

        await().atMost(3, TimeUnit.SECONDS)
                .until(() -> {
                    OrderState currentState = jsonToListOfOrders(orderController.getAllOrders().getBody().getMessage()).get(0).getState();
                    return OrderState.CANCELLED.equals(currentState);
                });

        List<Product> productsExpired = jsonToListOfProducts(productController.getAllProducts().getBody().getMessage());
        Assertions.assertThat(productsExpired.get(0).getQuantityInStock()).isEqualTo(100);
    }

    @Test
    void testInsufficientProductInStock() throws Exception {
        ProductRequest product = new ProductRequest("rohlik", 100, 1.90);
        ProductRequest product2 = new ProductRequest("maslo", 1, 59.90);
        productController.addNewProduct(product);
        productController.addNewProduct(product2);
        List<Product> products = jsonToListOfProducts(productController.getAllProducts().getBody().getMessage());
        Assertions.assertThat(products.size()).isEqualTo(2);

        ItemRequest orderItem1 = new ItemRequest("rohlik", 20);
        ItemRequest orderItem2 = new ItemRequest("maslo", 2);
        ItemRequest orderItem3 = new ItemRequest("sunka", 5);
        List<ItemRequest> orderItems = Arrays.asList(orderItem1, orderItem2, orderItem3);
        OrderRequest order = new OrderRequest(orderItems);

        List<ItemRequest> missingItems = jsonToListOfItemRequests(orderController.addNewOrder(order).getBody().getMessage());
        Assertions.assertThat(missingItems)
                .contains(new ItemRequest("maslo", 1))
                .contains(new ItemRequest("sunka", 5));

        List<Product> productsAvailable = jsonToListOfProducts(productController.getAllProducts().getBody().getMessage());
        Assertions.assertThat(productsAvailable).contains(new Product((long) 1, "rohlik", 100, 1.90));
    }

    @Test
    void testPayOrder() throws Exception {
        ProductRequest product = new ProductRequest("rohlik", 100, 1.90);
        productController.addNewProduct(product);

        ReflectionTestUtils.setField(orderService, "orderExpirationTime", 1);

        ItemRequest orderItem = new ItemRequest("rohlik", 20);
        List<ItemRequest> orderItems = Arrays.asList(orderItem);
        OrderRequest order = new OrderRequest(orderItems);

        orderController.addNewOrder(order);
        List<Order> orders = jsonToListOfOrders(orderController.getAllOrders().getBody().getMessage());
        Assertions.assertThat(orders).isNotEmpty();
        Assertions.assertThat(orders.get(0).getState()).isEqualTo(OrderState.NEW);

        List<Product> products = jsonToListOfProducts(productController.getAllProducts().getBody().getMessage());
        Assertions.assertThat(products.get(0).getQuantityInStock()).isEqualTo(80);

        orderController.payOrder(orders.get(0).getId());
        List<Order> ordersPaid = jsonToListOfOrders(orderController.getAllOrders().getBody().getMessage());
        Assertions.assertThat(ordersPaid.get(0).getState()).isEqualTo(OrderState.PAID);

        await().atMost(3, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(scheduleService, times(15)).scheduleFixedRateTask());

        List<Order> ordersExpired = jsonToListOfOrders(orderController.getAllOrders().getBody().getMessage());
        Assertions.assertThat(ordersExpired.get(0).getState()).isEqualTo(OrderState.PAID);

        List<Product> productsPaid = jsonToListOfProducts(productController.getAllProducts().getBody().getMessage());
        Assertions.assertThat(productsPaid.get(0).getQuantityInStock()).isEqualTo(80);
    }

    private List<Product> jsonToListOfProducts(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, new TypeReference<List<Product>>() {
        });
    }

    private List<Order> jsonToListOfOrders(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, new TypeReference<List<Order>>() {
        });
    }

    private List<ItemRequest> jsonToListOfItemRequests(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, new TypeReference<List<ItemRequest>>() {
        });
    }

}
