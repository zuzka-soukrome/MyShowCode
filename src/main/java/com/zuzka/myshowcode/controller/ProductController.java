package com.zuzka.myshowcode.controller;

import com.zuzka.myshowcode.dto.ApiResponse;
import com.zuzka.myshowcode.dto.ProductRequest;
import com.zuzka.myshowcode.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/product")
public class ProductController {

    private static final String SUCCESS_MESSAGE = "SUCCESS";

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping()
    public ResponseEntity<ApiResponse> addNewProduct(@RequestBody ProductRequest product) {
        productService.addNewProduct(product);
        return new ResponseEntity<>(new ApiResponse(SUCCESS_MESSAGE, "Order added"), HttpStatus.OK);
    }

    @PostMapping(path = "/update")
    public ResponseEntity<ApiResponse> updateProductById(@RequestBody ProductRequest product) {
        productService.updateProduct(product);
        return new ResponseEntity<>(new ApiResponse(SUCCESS_MESSAGE, "Order updated"), HttpStatus.OK);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<String> getAllProducts() {
        String response = productService.getAllProducts().stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/get/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        String message = productService.getProductById(id).orElseThrow().toString();
        return new ResponseEntity<>(new ApiResponse(SUCCESS_MESSAGE, message), HttpStatus.OK);
    }

    @GetMapping(path = "/getByName")
    public ResponseEntity<ApiResponse> getProductByName(@RequestParam String name) {
        String message = productService.getProductByName(name).orElseThrow().toString();
        return new ResponseEntity<>(new ApiResponse(SUCCESS_MESSAGE, message), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<ApiResponse> deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
        return new ResponseEntity<>(new ApiResponse(SUCCESS_MESSAGE, "Order cancelled"), HttpStatus.OK);
    }

}
