package com.gb.store.controller;

import com.gb.store.model.request.GroceryItemRequest;
import com.gb.store.model.request.GroceryItemUpdateRequest;
import com.gb.store.model.response.GroceryItemResponse;
import com.gb.store.service.GroceryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/admin/groceries")
@PreAuthorize("hasRole('ADMIN')")
@Validated
@Tag(name = "Grocery Management Api")
public class GroceryManagementRestController {

    private final GroceryStoreRestController groceryStore;
    private final GroceryService groceryService;

    @Autowired
    public GroceryManagementRestController(
            GroceryStoreRestController groceryStore,
            GroceryService groceryService
    ) {
        this.groceryStore = groceryStore;
        this.groceryService = groceryService;
    }


    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    @Operation(description = "Fetch all groceries from grocery store.")
    public ResponseEntity<Page<GroceryItemResponse>> getAllGroceries(
            @RequestParam(value = "page_number", defaultValue = "1", required = false) Integer pageNo,
            @RequestParam(value = "page_size", defaultValue = "1", required = false) Integer pageSize,
            @RequestParam(value = "sort_by", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sort_dir", defaultValue = "asc", required = false) String sortDir
    ) {
        return this.groceryStore.getAllGroceries(pageNo,pageSize,sortBy,sortDir);
    }


    @PostMapping
    @PreAuthorize("hasAuthority('admin:write')")
    @Operation(description = "Add multiple/single new grocery items to grocery store.")
    public ResponseEntity<Void> addGroceryItems(@RequestBody List<@Valid GroceryItemRequest> groceryItemsRequest) {
        groceryItemsRequest.forEach(groceryService::addGrocery);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PatchMapping
    @PreAuthorize("hasAuthority('admin:update')")
    @Operation(description = "Update existing grocery item details.")
    public ResponseEntity<GroceryItemResponse> updateGroceryItem(@RequestBody @Valid GroceryItemUpdateRequest updateRequest) {
        GroceryItemResponse updateGrocery = groceryService.updateGrocery(updateRequest);
        return new ResponseEntity<>(updateGrocery, HttpStatus.OK);
    }


    @DeleteMapping("/{grocery_item_id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    @Operation(description = "Delete existing grocery item from store.")
    public ResponseEntity<Void> deleteGrocery(@PathVariable("grocery_item_id") @NotNull String groceryItemId) {
        groceryService.deleteGrocery(groceryItemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
