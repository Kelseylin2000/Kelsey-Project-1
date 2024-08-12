package com.example.stylish.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    
    @NotNull
    private int id;

    private String name;

    @NotNull
    private BigDecimal price;
    
    private ColorDto color;

    @NotNull
    private String size;

    @NotNull
    private int qty;
}