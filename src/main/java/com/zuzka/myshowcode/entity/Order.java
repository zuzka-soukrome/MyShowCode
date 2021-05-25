package com.zuzka.myshowcode.entity;


import com.zuzka.myshowcode.enums.OrderState;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ElementCollection
    private List<Item> items;

    private OrderState state;

}
