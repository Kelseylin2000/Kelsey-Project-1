package com.example.stylish.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.stylish.dto.ColorDto;
import com.example.stylish.dto.ProductDto;
import com.example.stylish.dto.VariantDto;

@Repository
public class ProductManageRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveProduct(ProductDto product) {
        String sql = "INSERT INTO product (category, title, description, price, texture, wash, place, note, story, main_image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
            product.getCategory(),
            product.getTitle(),
            product.getDescription(),
            product.getPrice(),
            product.getTexture(),
            product.getWash(),
            product.getPlace(),
            product.getNote(),
            product.getStory(),
            product.getMainImage());

        // Save images and variants as well
        saveImages(product);
        saveVariants(product);
    }

    private void saveImages(ProductDto product) {
        if(product.getImages().get(0) != null){
            String sql = "INSERT INTO product_image (product_id, image_path) VALUES (?, ?)";

            // Assume the product ID is retrieved correctly
            Long productId = getProductIdByTitle(product.getTitle());

            for (String imagePath : product.getImages()) {
                jdbcTemplate.update(sql, productId, imagePath);
            }
        }
    }

    private void saveVariants(ProductDto product) {
        if(product.getVariants().get(0) != null){
            String sql = "INSERT INTO product_variant (product_id, color_code, size, stock) VALUES (?, ?, ?, ?)";

            // Assume the product ID is retrieved correctly
            Long productId = getProductIdByTitle(product.getTitle());

            for (VariantDto variant : product.getVariants()) {
                jdbcTemplate.update(sql, productId, variant.getColorCode(), variant.getSize(), variant.getStock());
            }
        }
    }

    private Long getProductIdByTitle(String title) {
        String sql = "SELECT product_id FROM product WHERE title = ? ORDER BY product_id DESC LIMIT 1";
        return jdbcTemplate.queryForObject(sql, new Object[]{title}, Long.class);
    }

    public boolean colorExists(String colorCode) {
        String sql = "SELECT COUNT(*) FROM color WHERE code = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{colorCode}, Integer.class);
        return count != null && count > 0;
    }

    public void saveColor(ColorDto color) {
        String sql = "INSERT INTO color (code, name) VALUES (?, ?)";
        jdbcTemplate.update(sql, color.getCode(), color.getName());
    }
}
