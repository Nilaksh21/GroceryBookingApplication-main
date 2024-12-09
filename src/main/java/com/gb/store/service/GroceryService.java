package com.gb.store.service;

import com.gb.store.exception.GroceryNotFoundException;
import com.gb.store.exception.InsufficientInventoryException;
import com.gb.store.model.request.GroceryItemRequest;
import com.gb.store.model.request.GroceryItemUpdateRequest;
import com.gb.store.model.response.GroceryItemResponse;
import com.gb.store.repo.GroceryItemRepository;
import com.gb.store.repo.entity.GroceryItem;
import com.gb.store.utils.GroceryItemMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class GroceryService {
    private final GroceryItemRepository groceryItemRepository;
    private final GroceryItemMapper groceryItemMapper;

    @Autowired
    public GroceryService(
            GroceryItemRepository groceryItemRepository,
            GroceryItemMapper groceryItemMapper
    ) {
        this.groceryItemRepository = groceryItemRepository;
        this.groceryItemMapper = groceryItemMapper;
    }

    public GroceryItem getGroceryItemById(String groceryItemId) {
        Optional<GroceryItem> groceryItemOptional = groceryItemRepository.findById(groceryItemId);
        if(groceryItemOptional.isEmpty()){
            log.error("Invalid Grocery Item ID.");
            throw new GroceryNotFoundException("Invalid Grocery Item ID.");
        }
        return groceryItemOptional.get();
    }

    @Transactional(readOnly = true)
    public Page<GroceryItemResponse> getAllGroceryItems(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);

        Page<GroceryItem> groceriesPage = groceryItemRepository.findAll(pageable);

        List<GroceryItemResponse> groceriesResponsePage
                = groceriesPage.map(this.groceryItemMapper::mapToGroceryItemResponse).toList();

        return new PageImpl<>(groceriesResponsePage,pageable,groceriesPage.getTotalElements());

    }


    @Transactional
    public void addGrocery(GroceryItemRequest groceryItemRequest) {
        log.info("Grocery Item Request : {}",groceryItemRequest);
        GroceryItem groceryItem = this.groceryItemMapper.mapToGroceryItem(groceryItemRequest);
        groceryItemRepository.save(groceryItem);
    }

    @Transactional
    public GroceryItemResponse updateGrocery(GroceryItemUpdateRequest updateRequest) {
        log.info("update request : {}",updateRequest);

        String groceryItemId = updateRequest.getId();
        Optional<GroceryItem> groceryItemOptional = groceryItemRepository.findById(groceryItemId);

        if(groceryItemOptional.isEmpty()){
            log.error("Invalid Grocery Item ID.");
            throw new GroceryNotFoundException("Invalid Grocery Item ID.");
        }

        GroceryItem groceryItem = groceryItemOptional.get();

        if(updateRequest.getName() != null)
            groceryItem.setName(updateRequest.getName());

        if(updateRequest.getPrice() != null)
            groceryItem.setPrice(updateRequest.getPrice());

        if(updateRequest.getQuantity() != null)
            groceryItem.setQuantity(updateRequest.getQuantity());

        GroceryItem groceryItemUpdated =  groceryItemRepository.save(groceryItem);

        return this.groceryItemMapper.mapToGroceryItemResponse(groceryItemUpdated);
    }

    @Transactional
    public void deleteGrocery(String groceryItemId) {
        log.error("Deleting Grocery Item Id : {}",groceryItemId);
        Optional<GroceryItem> groceryItemOptional = groceryItemRepository.findById(groceryItemId);
        if(groceryItemOptional.isEmpty()){
            log.error("Invalid Grocery Item ID.");
            throw new GroceryNotFoundException("Invalid Grocery Item ID.");
        }
        groceryItemRepository.deleteById(groceryItemId);
    }

    public GroceryItem manageInventory(String groceryItemId, int quantity) {

        GroceryItem groceryItem = getGroceryItemById(groceryItemId);
        Integer groceryItemQuantity = groceryItem.getQuantity();

        if(quantity > groceryItemQuantity){
            log.error("Order quantity is more than available quantity.");
            throw new InsufficientInventoryException("Ordered quantity is more than available quantity.");
        }

        groceryItem.setQuantity(groceryItemQuantity - quantity);
        return groceryItemRepository.save(groceryItem);
    }
}
