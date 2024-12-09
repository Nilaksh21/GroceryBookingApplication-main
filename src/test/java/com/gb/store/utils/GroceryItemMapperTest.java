package com.gb.store.utils;

import com.gb.store.model.request.GroceryItemRequest;
import com.gb.store.model.response.GroceryItemResponse;
import com.gb.store.repo.entity.GroceryItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class GroceryItemMapperTest {

    @Autowired
    private GroceryItemMapper groceryItemMapper;

    @Test
    void mapToGroceryItemResponse() {
        // Arrange
        GroceryItem groceryItem = GroceryItem.builder()
                .id("12345")
                .name("Soap")
                .quantity(2)
                .price(100.5)
                .build();

        GroceryItemResponse expectedResponse = GroceryItemResponse.builder()
                .id("12345")
                .name("Soap")
                .quantity(2)
                .price(100.5)
                .build();

        // call actual method
        GroceryItemResponse actualResponse = groceryItemMapper.mapToGroceryItemResponse(groceryItem);

        // Assert
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void mapToGroceryItem() {

        GroceryItemRequest groceryItemRequest = GroceryItemRequest.builder()
                .name("Soap")
                .quantity(2)
                .price(100.5)
                .build();

        GroceryItem groceryItem = GroceryItem.builder()
                .name("Soap")
                .quantity(2)
                .price(100.5)
                .build();

        //call actual method
        GroceryItem actualGroceryItem = groceryItemMapper.mapToGroceryItem(groceryItemRequest);

        // Assert
        assertEquals(groceryItem, actualGroceryItem);
    }
}