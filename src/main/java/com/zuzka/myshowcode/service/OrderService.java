package com.zuzka.myshowcode.service;

import com.zuzka.myshowcode.dto.OrderRequest;
import com.zuzka.myshowcode.entity.Order;
import com.zuzka.myshowcode.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderService {

    private OrderRepository repository;
    private ModelMapper modelMapper;

    public OrderService(OrderRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public void addNewOrder(OrderRequest order) {
        Order o = modelMapper.map(order, Order.class);
        log.info(o.toString());
        repository.save(modelMapper.map(order, Order.class));

    }

    public List<Order> getAllOrders () {
        return (List<Order>) repository.findAll();
    }

}
