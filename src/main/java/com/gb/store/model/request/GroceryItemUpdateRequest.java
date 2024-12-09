package com.gb.store.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class GroceryItemUpdateRequest {

    @NotNull
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    @Nullable
    private String name;

    @JsonProperty("quantity")
    @Nullable
    private Integer quantity;

    @JsonProperty("price")
    @Nullable
    private Double price;
}
