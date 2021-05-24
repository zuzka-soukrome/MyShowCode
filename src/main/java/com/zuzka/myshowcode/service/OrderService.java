package com.zuzka.myshowcode.service;

import com.zuzka.myshowcode.entity.Order;
import com.zuzka.myshowcode.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public List<Order> getAllOrders () {
        return (List<Order>) repository.findAll();
    }
 }
