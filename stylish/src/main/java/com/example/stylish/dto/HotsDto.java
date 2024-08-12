package com.example.stylish.dto;

import java.util.List;

public class HotsDto {
    private String title;
    private List<ProductDto> products;

    // No-argument constructor
    public HotsDto() {
    }

    // Parameterized constructor
    public HotsDto(String title, List<ProductDto> products) {
        this.title = title;
        this.products = products;
    }

    // Getter and Setter methods
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }
}