package com.zuzka.myshowcode.entity;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany
    private List<Item> items;


    @Entity
    @Data
    public class Item {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        @NotBlank(message = "Product name cannot be blank")
        private String productName;
        @Min(value = 0, message = "Quantity must be positive number")
        private int quantity;

    }
}
