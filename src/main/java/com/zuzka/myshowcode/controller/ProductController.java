package com.zuzka.myshowcode.controller;

import com.zuzka.myshowcode.model.Product;
import com.zuzka.myshowcode.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/product")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(path = "/getAll")
    public @ResponseBody String getAllProducts() {
        return productService.getAllProducts().stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
    }

    @GetMapping(path = "/getById")
    public @ResponseBody String getProductById(@RequestParam Long id) {
        return productService.getProductById(id).orElse(new Product()).toString();
    }

    @GetMapping(path = "/getByName")
    public @ResponseBody String getProductByName(@RequestParam String name) {
        return productService.getProductByName(name).orElse(new Product()).toString();
    }

    @PostMapping(path = "/add")
    public @ResponseBody String addNewProduct(@RequestParam String name, @RequestParam int quantityInStock, @RequestParam double pricePerUnit) {
        productService.addNewProduct(new Product(name, quantityInStock, pricePerUnit));
        return "New product saved";
    }

    @DeleteMapping(path = "/deleteById")
    public @ResponseBody String deleteProductById(@RequestParam Long id){
        productService.deleteProductById(id);
        return "Product deleted";
    }

    @DeleteMapping(path = "/deleteByName")
    @Transactional
    public @ResponseBody String deleteProductById(@RequestParam String name){
        productService.deleteProductByName(name);
        return "Product deleted";
    }

}
