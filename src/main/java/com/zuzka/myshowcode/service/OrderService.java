package com.zuzka.myshowcode.service;

import com.zuzka.myshowcode.dto.ItemRequest;
import com.zuzka.myshowcode.dto.OrderRequest;
import com.zuzka.myshowcode.entity.Order;
import com.zuzka.myshowcode.enums.OrderState;
import com.zuzka.myshowcode.repository.OrderRepository;
import com.zuzka.myshowcode.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderService {

    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private ModelMapper modelMapper;

    @Value("${order.expiration.seconds}")
    private int orderExpirationTime;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    public List<ItemRequest> addNewOrder(OrderRequest order) {
        List<ItemRequest> missingItems = getMissingItems(order);
        if(missingItems.isEmpty()){
            blockProductsInStock(order);
            orderRepository.save(modelMapper.map(order, Order.class));
            return new ArrayList<>();
        } else {
            return missingItems;
        }
    }

    public List<Order> getAllOrders() {
        return (List<Order>) orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public void payOrder(Long id) {
        var order = orderRepository.findById(id).orElseThrow();
        order.setState(OrderState.PAID);
        orderRepository.save(order);
    }

    public void cancelOrder(Long id) {
        var order = orderRepository.findById(id).orElseThrow();
        order.setState(OrderState.CANCELLED);
        orderRepository.save(order);
        unblockProductsInStock(order);
    }

    public void checkForExpiredOrders() {
        getAllOrders().stream()
                .filter(order -> OrderState.NEW.equals(order.getState()))
                .filter(order -> order.getCreateDateTime().until(LocalDateTime.now(), ChronoUnit.SECONDS) > orderExpirationTime)
                .forEach(order -> {
                    cancelOrder(order.getId());
                    log.info("Order with id={} has expired. Cancelling the order.", order.getId());
                });
    }

    private List<ItemRequest> getMissingItems(OrderRequest order) {
        List<ItemRequest> missingItems = new ArrayList<>();
        order.getItems().stream()
                .forEach(item -> {
                    var product = productRepository.findByName(item.getProductName()).orElse(null);
                    if (ObjectUtils.isEmpty(product)) {
                        missingItems.add(new ItemRequest(item.getProductName(), item.getQuantity()));
                    } else {
                        int difference = product.getQuantityInStock() - item.getQuantity();
                        if (difference < 0) {
                            missingItems.add(new ItemRequest(item.getProductName(), Math.abs(difference)));
                        }
                    }
                });
        return missingItems;
    }

    private void blockProductsInStock(OrderRequest order) {
        order.getItems().stream()
                .forEach(item -> {
                            var product = productRepository.findByName(item.getProductName()).orElse(null);
                            product.setQuantityInStock(product.getQuantityInStock() - item.getQuantity());
                            productRepository.save(product);
                        }
                );
    }

    private void unblockProductsInStock(Order order) {
        order.getItems().stream()
                .forEach(item -> {
                            var product = productRepository.findByName(item.getProductName()).orElse(null);
                            if (!ObjectUtils.isEmpty(product)) {
                                product.setQuantityInStock(product.getQuantityInStock() + item.getQuantity());
                                productRepository.save(product);
                            }
                        }
                );
    }

}
