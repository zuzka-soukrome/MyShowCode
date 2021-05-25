package com.zuzka.myshowcode.service;

import com.zuzka.myshowcode.dto.OrderRequest;
import com.zuzka.myshowcode.entity.Order;
import com.zuzka.myshowcode.enums.OrderState;
import com.zuzka.myshowcode.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderService {

    private OrderRepository repository;
    private ModelMapper modelMapper;

    @Value("${order.expiration.minutes}")
    private int orderExpirationTime;

    public OrderService(OrderRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public void addNewOrder(OrderRequest order) {
        repository.save(modelMapper.map(order, Order.class));

    }

    public List<Order> getAllOrders () {
        return (List<Order>) repository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return repository.findById(id);
    }

    public void payOrder(Long id){
        Order order = repository.findById(id).orElseThrow();
        order.setState(OrderState.PAID);
        repository.save(order);
    }

    public void cancelOrder(Long id) {
        Order order = repository.findById(id).orElseThrow();
        order.setState(OrderState.CANCELLED);
        repository.save(order);
    }

    @Value("${order.expiration.minutes}")
    public void checkForExpiredOrders() {
        getAllOrders().stream()
                .filter(order -> OrderState.NEW.equals(order.getState()))
                .filter(order -> order.getCreateDateTime().until(LocalDateTime.now(), ChronoUnit.MINUTES) > orderExpirationTime)
                .forEach(order -> {
                    cancelOrder(order.getId());
                    log.info("Order with id={} has expired. Cancelling the order.", order.getId());
                });
    }
}
