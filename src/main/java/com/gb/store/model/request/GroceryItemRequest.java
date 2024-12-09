package com.gb.store.model.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroceryItemRequest {
    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @Positive
    @JsonProperty("quantity")
    private Integer quantity;

    @NotNull
    @Positive
    @JsonProperty("price")
    private Double price;
}
