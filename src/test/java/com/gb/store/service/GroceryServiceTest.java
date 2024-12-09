package com.gb.store.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gb.store.exception.GroceryNotFoundException;
import com.gb.store.exception.InsufficientInventoryException;
import com.gb.store.model.request.GroceryItemRequest;
import com.gb.store.model.request.GroceryItemUpdateRequest;
import com.gb.store.model.response.GroceryItemResponse;
import com.gb.store.repo.GroceryItemRepository;
import com.gb.store.repo.entity.GroceryItem;
import com.gb.store.utils.GroceryItemMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class GroceryServiceTest {

    @Mock
    private GroceryItemRepository groceryItemRepository;

    @Mock
    private GroceryItemMapper groceryItemMapper;

    @InjectMocks
    private GroceryService groceryService;

    @Test
    void getAllGroceryItems() {
        //request

        int pageNo = 0;
        int pageSize = 1;
        String sortBy = "name";
        String sortDir = "asc";

        Sort sort = Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);

        GroceryItem groceryItem = GroceryItem.builder()
                .id("12345")
                .name("Soap")
                .quantity(5)
                .price(100.5)
                .build();

        List<GroceryItem> groceryItemList = List.of(groceryItem);

        GroceryItemResponse groceryItemResponse = GroceryItemResponse.builder()
                .id("12345")
                .name("Soap")
                .quantity(5)
                .price(100.5)
                .build();

        //mocking
        Mockito.when(groceryItemRepository.findAll(pageable)).thenReturn(new PageImpl<>(groceryItemList,pageable,1));
        Mockito.when(groceryItemMapper.mapToGroceryItemResponse(groceryItem)).thenReturn(groceryItemResponse);

        //call the service
        Page<GroceryItemResponse> responses = groceryService.getAllGroceryItems(pageNo,pageSize,sortBy,sortDir);

        assertEquals(1, responses.getTotalElements());
        assertEquals("12345", responses.getContent().get(0).getId());

    }

    @Test
    void addGrocery() {
        GroceryItemRequest groceryItemRequest = GroceryItemRequest.builder()
                .name("Soap")
                .quantity(5)
                .price(100.5)
                .build();

        GroceryItem groceryItem = GroceryItem.builder()
                .name("Soap")
                .quantity(5)
                .price(100.5)
                .build();

        //mocking
        Mockito.when(groceryItemRepository.save(groceryItem)).thenReturn(groceryItem);

        groceryService.addGrocery(groceryItemRequest);
    }

    @Test
    void updateGrocery_UpdateQuantity() {

        GroceryItemUpdateRequest groceryItemUpdateRequest = GroceryItemUpdateRequest.builder()
                .id("12345")
                .quantity(50)
                .build();

        GroceryItem groceryItem = GroceryItem.builder()
                .id("12345")
                .name("Soap")
                .quantity(5)
                .price(100.5)
                .build();

        GroceryItem updatedGroceryItem = GroceryItem.builder()
                .id("12345")
                .name("Soap")
                .quantity(50)
                .price(100.5)
                .build();

        GroceryItemResponse groceryItemResponse = GroceryItemResponse.builder()
                .id("12345")
                .name("Soap")
                .quantity(50)
                .price(100.5)
                .build();

        //mocking
        Mockito.when(groceryItemRepository.findById("12345")).thenReturn(Optional.of(groceryItem));
        Mockito.when(groceryItemRepository.save(updatedGroceryItem)).thenReturn(updatedGroceryItem);
        Mockito.when(groceryItemMapper.mapToGroceryItemResponse(updatedGroceryItem)).thenReturn(groceryItemResponse);

        GroceryItemResponse response = groceryService.updateGrocery(groceryItemUpdateRequest);

        assertEquals(50, response.getQuantity());
        assertEquals("12345", response.getId());
    }

    @Test
    void updateGrocery_NoUpdates() {

        GroceryItemUpdateRequest groceryItemUpdateRequest = GroceryItemUpdateRequest.builder()
                .id("12345")
                .build();

        GroceryItem groceryItem = GroceryItem.builder()
                .id("12345")
                .name("Soap")
                .quantity(5)
                .price(100.5)
                .build();

        GroceryItem updatedGroceryItem = GroceryItem.builder()
                .id("12345")
                .name("Soap")
                .quantity(5)
                .price(100.5)
                .build();

        GroceryItemResponse groceryItemResponse = GroceryItemResponse.builder()
                .id("12345")
                .name("Soap")
                .quantity(5)
                .price(100.5)
                .build();

        //mocking
        Mockito.when(groceryItemRepository.findById("12345")).thenReturn(Optional.of(groceryItem));
        Mockito.when(groceryItemRepository.save(updatedGroceryItem)).thenReturn(updatedGroceryItem);
        Mockito.when(groceryItemMapper.mapToGroceryItemResponse(updatedGroceryItem)).thenReturn(groceryItemResponse);

        GroceryItemResponse response = groceryService.updateGrocery(groceryItemUpdateRequest);

        assertEquals(5, response.getQuantity());
        assertEquals("12345", response.getId());
    }

    @Test
    void updateGrocery_GroceryNotFound() {

        GroceryItemUpdateRequest groceryItemUpdateRequest = GroceryItemUpdateRequest.builder()
                .id("INVALID")
                .build();

        //mocking
        Mockito.when(groceryItemRepository.findById("INVALID")).thenReturn(Optional.empty());

        //assert
        assertThrows(GroceryNotFoundException.class,
                () -> groceryService.updateGrocery(groceryItemUpdateRequest));
    }

    @Test
    void deleteGrocery() {
        String groceryItemId = "12345";

        GroceryItem groceryItem = GroceryItem.builder()
                .id("12345")
                .name("Soap")
                .quantity(5)
                .price(100.5)
                .build();

        Mockito.when(groceryItemRepository.findById(groceryItemId)).thenReturn(Optional.of(groceryItem));
        Mockito.doNothing().when(groceryItemRepository).deleteById(groceryItemId);

        groceryService.deleteGrocery(groceryItemId);
    }

    @Test
    void deleteGrocery_GroceryNotFound() {
        String groceryItemId = "INVALID";

        Mockito.when(groceryItemRepository.findById(groceryItemId)).thenReturn(Optional.empty());

        assertThrows(GroceryNotFoundException.class,
                () -> groceryService.deleteGrocery(groceryItemId));
    }

    @Test
    void getGroceryItemById() {

        String groceryItemId = "12345";

        GroceryItem groceryItem = GroceryItem.builder()
                .id("12345")
                .name("Soap")
                .quantity(5)
                .price(100.5)
                .build();

        Mockito.when(groceryItemRepository.findById(groceryItemId)).thenReturn(Optional.of(groceryItem));

        GroceryItem response = groceryService.getGroceryItemById(groceryItemId);

        assertEquals(groceryItemId, response.getId());
        assertEquals("Soap", response.getName());
    }

    @Test
    void getGroceryItemById_GroceryNotFound() {

        String groceryItemId = "INVALID";

        Mockito.when(groceryItemRepository.findById(groceryItemId)).thenReturn(Optional.empty());

        assertThrows(GroceryNotFoundException.class,
                () -> groceryService.getGroceryItemById(groceryItemId));
    }

    @Test
    void manageInventory() {

        String groceryItemId = "12345";
        int deductQuantity = 3;

        GroceryItem groceryItem = GroceryItem.builder()
                .id("12345")
                .name("Soap")
                .quantity(5)
                .price(100.5)
                .build();

        GroceryItem updatedGroceryItem = GroceryItem.builder()
                .id("12345")
                .name("Soap")
                .quantity(2)
                .price(100.5)
                .build();

        Mockito.when(groceryItemRepository.findById(groceryItemId)).thenReturn(Optional.of(groceryItem));
        Mockito.when(groceryItemRepository.save(updatedGroceryItem)).thenReturn(updatedGroceryItem);

        GroceryItem response = groceryService.manageInventory(groceryItemId, deductQuantity);

        assertEquals(groceryItemId, response.getId());
        assertEquals(2, response.getQuantity());
    }

    @Test
    void manageInventory_GroceryNotFound() {

        String groceryItemId = "INVALID";
        int deductQuantity = 3;


        Mockito.when(groceryItemRepository.findById(groceryItemId)).thenReturn(Optional.empty());

        assertThrows(GroceryNotFoundException.class,
                () -> groceryService.manageInventory(groceryItemId, deductQuantity));
    }

    @Test
    void manageInventory_InsufficientInventory() {

        String groceryItemId = "12345";
        int deductQuantity = 10;

        GroceryItem groceryItem = GroceryItem.builder()
                .id("12345")
                .name("Soap")
                .quantity(5)
                .price(100.5)
                .build();


        Mockito.when(groceryItemRepository.findById(groceryItemId)).thenReturn(Optional.of(groceryItem));

        assertThrows(InsufficientInventoryException.class,
                () -> groceryService.manageInventory(groceryItemId, deductQuantity));

    }

}