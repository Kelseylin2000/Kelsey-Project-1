package com.example.stylish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.stylish.dto.DataResponseDto;
import com.example.stylish.dto.ProductDto;
import com.example.stylish.dto.ProductListResponseDto;
import com.example.stylish.service.ProductListService;

import jakarta.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping("/api/1.0/products")
public class ProductListController {

    @Autowired
    private ProductListService productListService;

    @GetMapping("/{category}")
    public ResponseEntity<ProductListResponseDto> getProductList(@PathVariable String category, @RequestParam(defaultValue = "0") int paging) {
        ProductListResponseDto productList = productListService.getProductListByCategory(category, paging);
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/search")
    public ResponseEntity<ProductListResponseDto> getProductListByTitle(@RequestParam(required = true) @NotNull String keyword, @RequestParam(defaultValue = "0") int paging) {
        ProductListResponseDto productList = productListService.getProductListByTitle(keyword, paging);
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/details")
    public ResponseEntity<DataResponseDto<ProductDto>> getProductListById(@RequestParam(required = true) @NotNull String id, @RequestParam(defaultValue = "0") int paging) {
        DataResponseDto<ProductDto> product = productListService.getProductListById(id, paging);
        return ResponseEntity.ok(product);
    }
}
