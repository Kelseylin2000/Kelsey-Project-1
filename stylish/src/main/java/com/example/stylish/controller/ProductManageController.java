package com.example.stylish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.stylish.dto.ProductDto;
import com.example.stylish.service.ProductManageService;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProductManageController {

    @Autowired
    private ProductManageService productManageService;

    @GetMapping("/admin/product.html")
    public String productManage(){
        return "productUpload";
    }

    @PostMapping("/api/1.0/admin/uploadProduct")
    public String uploadProduct(@ModelAttribute ProductDto product, @RequestParam MultipartFile mainImageFile, @RequestParam MultipartFile[] imagesFiles, Model model, RedirectAttributes redirectAttributes) {

        try {
            boolean success = productManageService.uploadProduct(product, mainImageFile, imagesFiles);
            if (success) {
                redirectAttributes.addFlashAttribute("message", "Successfully uploaded.");
            } else {
                redirectAttributes.addFlashAttribute("message", "Failed to upload.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error occurred: " + e.getMessage());
        }
        
        return "redirect:/admin/product.html";
    }
}
