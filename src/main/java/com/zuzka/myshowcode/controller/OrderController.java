package com.zuzka.myshowcode.controller;

import com.zuzka.myshowcode.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/order")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(path = "/getAll")
    public String getAllProducts() {
        return orderService.getAllOrders().stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
    }

}
