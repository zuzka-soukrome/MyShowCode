package com.zuzka.myshowcode.entity;

import lombok.Data;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class Item {

    private String productName;
    private Integer quantity;

}
