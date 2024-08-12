package com.example.stylish.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
        
    @NotBlank(message = "Prime is mandatory")
    private String prime;

    private OrderDto order;
}