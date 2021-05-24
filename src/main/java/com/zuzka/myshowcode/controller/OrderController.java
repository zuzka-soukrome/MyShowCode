package com.zuzka.myshowcode.controller;

import com.zuzka.myshowcode.dto.ApiResponse;
import com.zuzka.myshowcode.dto.OrderRequest;
import com.zuzka.myshowcode.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping(path = "/order")
public class OrderController {

    private static final String SUCCESS_MESSAGE = "SUCCESS";

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(path = "/add")
    public ResponseEntity<ApiResponse> addNewOrder(@RequestBody OrderRequest order) {
        log.info(order.toString());
        orderService.addNewOrder(order);
        return new ResponseEntity<>(new ApiResponse(SUCCESS_MESSAGE, "Product added"), HttpStatus.OK);
    }

    @GetMapping(path = "/getAll")
    public String getAllProducts() {
        return orderService.getAllOrders().stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
    }

}
