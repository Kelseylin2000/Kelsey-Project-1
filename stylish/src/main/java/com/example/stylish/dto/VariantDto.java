package com.example.stylish.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VariantDto {
    @JsonProperty("color_code")
    private String colorCode;
    private String size;
    private Integer stock;

    // Default constructor
    public VariantDto() {}

    // Parameterized constructor
    public VariantDto(String colorCode, String size, Integer stock) {
        this.colorCode = colorCode;
        this.size = size;
        this.stock = stock;
    }

    // Getters and Setters

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
