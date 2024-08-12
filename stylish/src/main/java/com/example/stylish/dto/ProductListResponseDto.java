package com.example.stylish.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductListResponseDto {

    private List<ProductDto> data;

    @JsonProperty("next_paging")
    private Integer nextPaging;

    // Default constructor
    public ProductListResponseDto() {
    }

    // Parameterized constructor
    public ProductListResponseDto(List<ProductDto> data, Integer nextPaging) {
        this.data = data;
        this.nextPaging = nextPaging;
    }

    // Getter for data
    public List<ProductDto> getData() {
        return data;
    }

    // Setter for data
    public void setData(List<ProductDto> data) {
        this.data = data;
    }

    // Getter for nextPaging
    public Integer getNextPaging() {
        return nextPaging;
    }

    // Setter for nextPaging
    public void setNextPaging(Integer nextPaging) {
        this.nextPaging = nextPaging;
    }
}
