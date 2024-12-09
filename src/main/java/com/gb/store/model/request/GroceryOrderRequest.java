package com.gb.store.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class GroceryOrderRequest {

    @NotNull
    @JsonProperty("grocer_item_id")
    private String groceryItemId;

    @NotNull
    @JsonProperty("quantity")
    private Integer quantity;
}
