package com.zuzka.myshowcode.entity;


import com.zuzka.myshowcode.enums.OrderState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Item> items;

    private OrderState state = OrderState.NEW;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createDateTime;

}
