package com.zuzka.myshowcode.controller;

import com.zuzka.myshowcode.dto.ApiResponse;
import com.zuzka.myshowcode.dto.ItemRequest;
import com.zuzka.myshowcode.dto.OrderRequest;
import com.zuzka.myshowcode.entity.Order;
import com.zuzka.myshowcode.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/orders")
public class OrderController {

    private static final String SUCCESS_MESSAGE = "SUCCESS";

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    @Operation(summary = "Create a new order")
    public ResponseEntity<ApiResponse> addNewOrder(@RequestBody OrderRequest order) {
        List<ItemRequest> missingItems = orderService.addNewOrder(order);
        if (ObjectUtils.isEmpty(missingItems)) {
            return new ResponseEntity<>(new ApiResponse(SUCCESS_MESSAGE, "Order added"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse("ITEMS_MISSING", missingItems.toString()), HttpStatus.OK);
        }
    }

    @GetMapping()
    @Operation(summary = "Get all orders")
    public ResponseEntity<ApiResponse> getAllOrders() {
        List<Order> allOrders = orderService.getAllOrders();
        return new ResponseEntity<>(new ApiResponse(SUCCESS_MESSAGE, allOrders.toString()), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Get an order by ID")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long id) {
        var message = orderService.getOrderById(id).orElseThrow().toString();
        return new ResponseEntity<>(new ApiResponse(SUCCESS_MESSAGE, message), HttpStatus.OK);
    }

    @PatchMapping(path = "/pay/{id}")
    @Operation(summary = "Pay an order by ID")
    public ResponseEntity<ApiResponse> payOrder(@PathVariable Long id) {
        orderService.payOrder(id);
        return new ResponseEntity<>(new ApiResponse(SUCCESS_MESSAGE, "Order paid"), HttpStatus.OK);
    }

    @PatchMapping(path = "/cancel/{id}")
    @Operation(summary = "Cancel an order by ID")
    public ResponseEntity<ApiResponse> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return new ResponseEntity<>(new ApiResponse(SUCCESS_MESSAGE, "Order cancelled"), HttpStatus.OK);
    }

}
