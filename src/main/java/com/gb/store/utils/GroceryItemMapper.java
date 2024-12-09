package com.gb.store.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gb.store.model.request.GroceryItemRequest;
import com.gb.store.model.response.GroceryItemResponse;
import com.gb.store.repo.entity.GroceryItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class GroceryItemMapper {

    private final ObjectMapper objectMapper;

    @Autowired
    public GroceryItemMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public GroceryItemResponse mapToGroceryItemResponse(GroceryItem groceryItem){
        return objectMapper.convertValue(groceryItem,GroceryItemResponse.class);
    }

    public GroceryItem mapToGroceryItem(GroceryItemRequest groceryItemRequest){
        return objectMapper.convertValue(groceryItemRequest,GroceryItem.class);
    }

}
