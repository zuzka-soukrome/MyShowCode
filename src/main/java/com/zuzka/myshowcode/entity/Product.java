package com.zuzka.myshowcode.entity;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "products", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;
    @Min(value = 0, message = "Quantity in stock must be positive number")
    private int quantityInStock;
    @Min(value = 0, message = "Price per unit must be positive number")
    private double pricePerUnit;

}