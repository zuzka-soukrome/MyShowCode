package com.zuzka.myshowcode.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemRequest {

    private String productName;
    private int quantity;

}
