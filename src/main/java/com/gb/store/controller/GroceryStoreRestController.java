package com.gb.store.controller;

import com.gb.store.constant.Constant;
import com.gb.store.model.request.GroceryOrderRequest;
import com.gb.store.model.response.GroceryItemResponse;
import com.gb.store.model.response.GroceryOrderResponse;
import com.gb.store.service.GroceryService;
import com.gb.store.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/groceries")
@Tag(name = "Grocery Store Api")
public class GroceryStoreRestController {

    private final OrderService orderService;
    private final GroceryService groceryService;

    @Autowired
    public GroceryStoreRestController(OrderService orderService, GroceryService groceryService) {
        this.orderService = orderService;
        this.groceryService = groceryService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('user:read')")
    @Operation(description = "Fetch all groceries from grocery store.")
    public ResponseEntity<Page<GroceryItemResponse>> getAllGroceries(
            @RequestParam(value = "page_no", defaultValue = "1", required = false) Integer pageNo,
            @RequestParam(value = "page_size", defaultValue = "1", required = false) Integer pageSize,
            @RequestParam(value = "sort_by", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sort_dir", defaultValue = "asc", required = false) String sortDir
    ) {
        //since the pageNo is 0th index reduce the page number by one
        Page<GroceryItemResponse> groceryItemResponses
                = groceryService.getAllGroceryItems(pageNo-1,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(groceryItemResponses, HttpStatus.OK);
    }

    @PostMapping("/order")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(description = "Place an order or groceries.")
    public ResponseEntity<GroceryOrderResponse> placeOrder(
            @RequestBody @Valid List<GroceryOrderRequest> groceryOrderRequest) {
        orderService.placeOrder(groceryOrderRequest);
        return new ResponseEntity<>(GroceryOrderResponse.builder().status(Constant.SUCCESS).build(), HttpStatus.CREATED);
    }

}
