package com.gb.store.repo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "ORDER_INFO")
public class OrderInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ORDER_ID")
    private String id;

    @OneToMany(mappedBy = "itemOrderId", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
}
