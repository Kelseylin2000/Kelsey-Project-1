package com.example.stylish.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.stylish.dto.ColorDto;
import com.example.stylish.dto.ProductDto;
import com.example.stylish.repository.ProductManageRepository;

import java.util.Arrays;

@Service
public class ProductManageService {

    private ProductManageRepository productManageRepository;
    private ImageSaveService imageSaveService;

    public ProductManageService(ProductManageRepository productManageRepository, ImageSaveService imageSaveService){
        this.productManageRepository = productManageRepository;
        this.imageSaveService = imageSaveService;
    }

    public boolean uploadProduct(ProductDto product, MultipartFile mainImageFile, MultipartFile[] imagesFiles) {
        String mainImagePath = imageSaveService.saveImage(mainImageFile, "main");
        product.setMainImage(mainImagePath);

        String[] imagePaths = imageSaveService.saveImages(imagesFiles, "additional");
        product.setImages(Arrays.asList(imagePaths));

        for (ColorDto color : product.getColors()) {
            handleColor(color);
        }

        productManageRepository.saveProduct(product);
        return true;
    }

    private void handleColor(ColorDto color) {
        if (!productManageRepository.colorExists(color.getCode())) {
            productManageRepository.saveColor(color);
        }
    }
}