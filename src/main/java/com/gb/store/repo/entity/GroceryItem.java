package com.gb.store.repo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="GROCERY_ITEM")
public class GroceryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "GROCERY_ID")
    private String id;

    @Column(name = "NAME",unique = true)
    private String name;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "PRICE")
    private Double price;
}
