package com.zuzka.myshowcode.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    private List<ItemRequest> items;

}
