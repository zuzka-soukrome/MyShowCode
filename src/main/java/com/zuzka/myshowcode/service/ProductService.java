package com.zuzka.myshowcode.service;

import com.zuzka.myshowcode.model.Product;
import com.zuzka.myshowcode.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAllProducts(){
        return (List<Product>) repository.findAll();
    }

    public void addNewProduct(Product product) {
        repository.save(product);
    }
}
