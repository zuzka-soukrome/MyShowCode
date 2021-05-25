package com.zuzka.myshowcode.service;

import com.zuzka.myshowcode.dto.OrderRequest;
import com.zuzka.myshowcode.entity.Order;
import com.zuzka.myshowcode.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private OrderRepository repository;
    private ModelMapper modelMapper;

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
        //TODO
    }

    public void deleteProductById(Long id) {
        repository.deleteById(id);
    }
}
