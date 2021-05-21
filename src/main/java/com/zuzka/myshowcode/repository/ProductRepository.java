package com.zuzka.myshowcode.repository;

import com.zuzka.myshowcode.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    Optional<Product> findByName(String name);
    void deleteByName(String name);

}
