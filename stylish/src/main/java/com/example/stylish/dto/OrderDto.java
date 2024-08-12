package com.example.stylish.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    @Size(max = 50)
    private String shipping;

    @Size(max = 50)
    private String payment;

    @NotNull
    private BigDecimal subtotal;

    @NotNull
    private BigDecimal freight;

    @NotNull
    private BigDecimal total;

    private RecipientDto recipient;

    private List<OrderItemDto> list;
}
