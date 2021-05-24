package com.zuzka.myshowcode.dto;

import lombok.Data;

@Data
public class ProductRequest {

    private String name;
    private int quantityInStock;
    private double pricePerUnit;

}
