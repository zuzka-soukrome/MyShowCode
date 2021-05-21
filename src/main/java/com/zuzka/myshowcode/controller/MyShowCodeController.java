package com.zuzka.myshowcode.controller;

import com.zuzka.myshowcode.model.Product;
import com.zuzka.myshowcode.service.OrderService;
import com.zuzka.myshowcode.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyShowCodeController {

    private OrderService orderService;
    private ProductService productService;

    public MyShowCodeController(OrderService orderService, ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;
    }

    @PostMapping(path = "/product/add")
    public @ResponseBody
    String addNewProduct(@RequestParam String name, @RequestParam int quantityInStock, @RequestParam double pricePerUnit) {
        Product product = new Product();
        product.setName(name);
        product.setQuantityInStock(quantityInStock);
        product.setPricePerUnit(pricePerUnit);
        productService.addNewProduct(product);
        return "New product saved";
    }

}
