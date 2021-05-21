package com.zuzka.myshowcode.service;

import com.zuzka.myshowcode.model.Product;
import com.zuzka.myshowcode.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAllProducts(){
        return (List<Product>) repository.findAll();
    }

    public Optional<Product> getProductById(Long id){
        return repository.findById(id);
    }

    public Optional<Product> getProductByName(String name){
        return repository.findByName(name);
    }

    public void addNewProduct(Product product) {
        repository.save(product);
    }

    public void deleteProductById(Long id){
        repository.deleteById(id);
    }

    public void deleteProductByName(String name){
        repository.deleteByName(name);
    }
}
