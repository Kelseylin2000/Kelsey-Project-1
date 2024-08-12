package com.example.stylish.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.stylish.dto.DataResponseDto;
import com.example.stylish.dto.OrderNumberDto;
import com.example.stylish.dto.OrderRequestDto;
import com.example.stylish.service.OrderService;

import jakarta.validation.Valid;

@Controller
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping("/admin/checkout.html")
    public String checkoutPage() {
        return "checkout";
    }

    @PostMapping("/api/1.0/order/checkout")
    public ResponseEntity<DataResponseDto<OrderNumberDto>> checkout(@Valid @RequestBody OrderRequestDto OrderRequest, @RequestHeader("Authorization") String token) {
        DataResponseDto<OrderNumberDto> response = orderService.checkout(OrderRequest, token);
        return ResponseEntity.ok(response);
    }
}
