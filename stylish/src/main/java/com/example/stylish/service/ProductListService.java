package com.example.stylish.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stylish.dto.DataResponseDto;
import com.example.stylish.dto.ProductDto;
import com.example.stylish.dto.ProductListResponseDto;
import com.example.stylish.repository.ProductListRepository;
import com.example.stylish.exception.BadRequestException;

@Service
public class ProductListService {

    @Autowired
    private ProductListRepository productListRepository;

    private static final List<String> VALID_CATEGORIES = Arrays.asList("all", "men", "women", "accessories");
    private static final int PAGE_SIZE = 6;

    public ProductListResponseDto getProductListByCategory(String category, int paging) {
        if (!VALID_CATEGORIES.contains(category)) {
            throw new BadRequestException("Invalid category: " + category);
        }

        List<ProductDto> products = productListRepository.getProductList(category, paging, 1);
        return processProductListResponse(products, paging);
    }

    public ProductListResponseDto getProductListByTitle(String keyword, int paging) {
        if (keyword == null || keyword.isEmpty()) {
            throw new BadRequestException("Keyword cannot be null or empty");
        }

        List<ProductDto> products = productListRepository.getProductList(keyword, paging, 2);
        return processProductListResponse(products, paging);
    }

    public DataResponseDto<ProductDto> getProductListById(String id, int paging) {
        if (id == null || id.isEmpty()) {
            throw new BadRequestException("ID cannot be null or empty");
        }

        List<ProductDto> products = productListRepository.getProductList(id, paging, 3);
        if (products == null || products.isEmpty()) {
            throw new BadRequestException("Product not found with the given ID");
        }
        ProductDto product = products.get(0);
        return new DataResponseDto<>(product);        
    }

    private ProductListResponseDto processProductListResponse(List<ProductDto> products, int paging) {
        Integer nextPage = null;
        if (products.size() > PAGE_SIZE) {
            nextPage = paging + 1;
            products = products.subList(0, PAGE_SIZE);
        }
        return new ProductListResponseDto(products, nextPage);
    }
}