package com.zuzka.myshowcode.controller;

import com.zuzka.myshowcode.dto.ApiResponse;
import com.zuzka.myshowcode.dto.OrderRequest;
import com.zuzka.myshowcode.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping()
    public ResponseEntity<ApiResponse> addNewOrder(@RequestBody OrderRequest order) {
        orderService.addNewOrder(order);
        return new ResponseEntity<>(new ApiResponse(SUCCESS_MESSAGE, "Product added"), HttpStatus.OK);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<String> getAllProducts() {
        String response = orderService.getAllOrders().stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/get/{id}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long id) {
        String message = orderService.getOrderById(id).orElseThrow().toString();
        return new ResponseEntity<>(new ApiResponse(SUCCESS_MESSAGE, message), HttpStatus.OK);
    }

    @GetMapping(path = "/pay/{id}")
    public ResponseEntity<ApiResponse> payOrder(@PathVariable Long id) {
        orderService.payOrder(id);
        return new ResponseEntity<>(new ApiResponse(SUCCESS_MESSAGE, "Order paid"), HttpStatus.OK);
    }

    @DeleteMapping(path = "/cancel/{id}")
    public ResponseEntity<ApiResponse> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return new ResponseEntity<>(new ApiResponse(SUCCESS_MESSAGE, "Product deleted"), HttpStatus.OK);
    }

}
