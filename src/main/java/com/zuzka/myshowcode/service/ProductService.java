package com.zuzka.myshowcode.service;

import com.zuzka.myshowcode.dto.ProductRequest;
import com.zuzka.myshowcode.entity.Product;
import com.zuzka.myshowcode.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository repository;
    private ModelMapper modelMapper;

    public ProductService(ProductRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public void addNewProduct(ProductRequest product) {
        repository.save(modelMapper.map(product, Product.class));
    }

    public void updateProduct(ProductRequest update) {
        Product product = getProductByName(update.getName()).orElseThrow();
        modelMapper.map(update, product);
        repository.save(product);
    }

    public List<Product> getAllProducts() {
        return (List<Product>) repository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return repository.findById(id);
    }

    public Optional<Product> getProductByName(String name) {
        return repository.findByName(name);
    }

    public void deleteProductById(Long id) {
        repository.deleteById(id);
    }
}
