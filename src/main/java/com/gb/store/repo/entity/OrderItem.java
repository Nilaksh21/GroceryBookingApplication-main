package com.gb.store.repo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "ORDER_ITEM")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ORDER_ITEM_ID")
    private String itemOrderId;

    @Column(name = "ORDER_QUANTITY")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "id")
    private GroceryItem groceryItem;

}
