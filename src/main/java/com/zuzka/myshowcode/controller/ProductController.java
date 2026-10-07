package com.zuzka.myshowcode.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzka.myshowcode.dto.ApiResponse;
import com.zuzka.myshowcode.dto.ProductRequest;
import com.zuzka.myshowcode.entity.Product;
import com.zuzka.myshowcode.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/products")
public class ProductController {

    private static final String SUCCESS_MESSAGE = "SUCCESS";

    private ProductService productService;
    private ObjectMapper objectMapper;

    public ProductController(ProductService productService, ObjectMapper objectMapper) {
        this.productService = productService;
        this.objectMapper = objectMapper;
    }

    @PostMapping()
    @Operation(summary = "Add a new product")
    public ResponseEntity<ApiResponse> addNewProduct(@RequestBody ProductRequest product) {
        productService.addNewProduct(product);
        return new ResponseEntity<>(new ApiResponse(SUCCESS_MESSAGE, "Product added"), HttpStatus.OK);
    }

    @PutMapping()
    @Operation(summary = "Update an existing product")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductRequest product) {
        productService.updateProduct(product);
        return new ResponseEntity<>(new ApiResponse(SUCCESS_MESSAGE, "Product updated"), HttpStatus.OK);
    }

    @GetMapping()
    @Operation(summary = "Get all products")
    public ResponseEntity<ApiResponse> getAllProducts() throws JsonProcessingException {
        List<Product> allProducts = productService.getAllProducts();
        return new ResponseEntity<>(new ApiResponse(SUCCESS_MESSAGE, objectMapper.writeValueAsString(allProducts)), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Get a product by ID")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) throws JsonProcessingException {
        var message = productService.getProductById(id).orElseThrow();
        return new ResponseEntity<>(new ApiResponse(SUCCESS_MESSAGE, objectMapper.writeValueAsString(message)), HttpStatus.OK);
    }

    @GetMapping(path = "/getByName")
    @Operation(summary = "Get a product by name")
    public ResponseEntity<ApiResponse> getProductByName(@RequestParam String name) throws JsonProcessingException {
        var message = productService.getProductByName(name).orElseThrow();
        return new ResponseEntity<>(new ApiResponse(SUCCESS_MESSAGE, objectMapper.writeValueAsString(message)), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete a product by ID")
    public ResponseEntity<ApiResponse> deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
        return new ResponseEntity<>(new ApiResponse(SUCCESS_MESSAGE, "Product deleted"), HttpStatus.OK);
    }

}
