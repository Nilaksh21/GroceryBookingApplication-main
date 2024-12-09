package com.gb.store.service;

import com.gb.store.model.request.GroceryOrderRequest;
import com.gb.store.repo.OrderRepository;
import com.gb.store.repo.entity.GroceryItem;
import com.gb.store.repo.entity.OrderInfo;
import com.gb.store.repo.entity.OrderItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private GroceryService groceryService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void placeOrder() {

        GroceryOrderRequest groceryOrderRequest = GroceryOrderRequest.builder()
                .groceryItemId("12345")
                .quantity(3)
                .build();

        List<GroceryOrderRequest> groceryOrderRequestList = List.of(groceryOrderRequest);

        GroceryItem groceryItem = GroceryItem.builder()
                .id("12345")
                .name("Soap")
                .quantity(2)
                .price(100.5)
                .build();

        OrderItem orderedItem = OrderItem.builder()
                .groceryItem(groceryItem)
                .quantity(3)
                .build();

        List<OrderItem> orderItems = List.of(orderedItem);

        OrderInfo orderInfo = OrderInfo.builder()
                .orderItems(orderItems)
                .build();

        OrderInfo orderInfoResponse = OrderInfo.builder()
                .id("23456")
                .orderItems(orderItems)
                .build();

        //mocking
        Mockito.when(groceryService.manageInventory("12345", 3)).thenReturn(groceryItem);
        Mockito.when(orderRepository.save(orderInfo)).thenReturn(orderInfoResponse);

        orderService.placeOrder(groceryOrderRequestList);
    }
}