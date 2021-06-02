package com.zuzka.myshowcode.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzka.myshowcode.dto.ItemRequest;
import com.zuzka.myshowcode.dto.OrderRequest;
import com.zuzka.myshowcode.entity.Item;
import com.zuzka.myshowcode.entity.Order;
import com.zuzka.myshowcode.enums.OrderState;
import com.zuzka.myshowcode.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void addNewOrder() throws Exception {
        ItemRequest orderItem = new ItemRequest("rohlik", 50);
        List<ItemRequest> orderItems = Arrays.asList(orderItem);
        OrderRequest order = new OrderRequest(orderItems);

        mockMvc.perform(post("/orders")
                .content(objectMapper.writeValueAsString(order))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"status\":\"SUCCESS\",\"message\":\"Order added\"}"));
    }

    @Test
    void getAllOrders() throws Exception {
        Item item = new Item("rohlik", 50);
        List<Item> items = Arrays.asList(item);
        Order order = new Order((long) 1, items, OrderState.NEW, LocalDateTime.of(2021, 1, 1, 0, 0));
        List<Order> orders = Arrays.asList(order);

        Mockito.when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"status\":\"SUCCESS\",\"message\":\"[{\\\"id\\\":1,\\\"items\\\":[{\\\"productName\\\":\\\"rohlik\\\",\\\"quantity\\\":50}],\\\"state\\\":\\\"NEW\\\",\\\"createDateTime\\\":\\\"2021-01-01T00:00:00\\\"}]\"}"));
    }

    @Test
    void getOrderById() throws Exception {
        Item item = new Item("rohlik", 50);
        List<Item> items = Arrays.asList(item);
        Order order = new Order((long) 1, items, OrderState.NEW, LocalDateTime.of(2021, 1, 1, 0, 0));

        Mockito.when(orderService.getOrderById(Mockito.anyLong())).thenReturn(Optional.of(order));

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"status\":\"SUCCESS\",\"message\":\"{\\\"id\\\":1,\\\"items\\\":[{\\\"productName\\\":\\\"rohlik\\\",\\\"quantity\\\":50}],\\\"state\\\":\\\"NEW\\\",\\\"createDateTime\\\":\\\"2021-01-01T00:00:00\\\"}\"}"));

    }

    @Test
    void payOrder() throws Exception {
        mockMvc.perform(patch("/orders/pay/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"status\":\"SUCCESS\",\"message\":\"Order paid\"}"));
    }

    @Test
    void cancelOrder() throws Exception {
        mockMvc.perform(patch("/orders/cancel/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"status\":\"SUCCESS\",\"message\":\"Order cancelled\"}"));
    }
}