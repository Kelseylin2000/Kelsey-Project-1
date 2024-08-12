package com.example.stylish.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.stylish.dto.ColorDto;
import com.example.stylish.dto.ProductDto;
import com.example.stylish.dto.VariantDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;



@Repository
public class ProductListRepository {

    @Value("${storage.location}")
    private String storageLocation;

    @Value("${elastic.ip}")
    private String elasticIp;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final int PAGE_SIZE = 6;

    // mode
    // 1 for list
    // 2 for search by title
    // 3 for search by id
    public List<ProductDto> getProductList(String target, int page, int mode) {


        int offset = page * PAGE_SIZE;
        String baseSql = "SELECT p.product_id, p.category, p.title, p.description, p.price, p.texture, p.wash, p.place, p.note, p.story, p.main_image " +
                         "FROM product p ";

        String sql;
        Object[] params;

        if(mode == 1){
            String category = target;

            if ("all".equalsIgnoreCase(category)) {
                sql = baseSql + "LIMIT ? OFFSET ?";
                params = new Object[]{PAGE_SIZE + 1, offset}; // addtional one page is for checking if any next page exists
            } else {
                sql = baseSql + "WHERE p.category = ? LIMIT ? OFFSET ?";
                params = new Object[]{category, PAGE_SIZE + 1, offset}; // addtional one page is for checking if any next page exists
            }
            
        }else if(mode == 2) {
            String keyword = "%" + target + "%";
            
            sql = baseSql + "WHERE p.title LIKE ? LIMIT ? OFFSET ?";
            params = new Object[]{keyword, PAGE_SIZE + 1, offset}; // addtional one page is for checking if any next page exists
            
        }else{
            String id = target;
            
            sql = baseSql + "WHERE p.product_id = ?";
            params = new Object[]{id};
        }


        List<ProductDto> products = jdbcTemplate.query(sql, params, new ProductRowMapper());

        // Fetch additional details for each product
        for (ProductDto product : products) {
            fetchProductDetails(product);
        }

        return products;
    }

    private void fetchProductDetails(ProductDto product) {
        // Fetch colors
        String colorSql = "SELECT DISTINCT c.code, c.name FROM color c " +
                          "JOIN product_variant pv ON pv.color_code = c.code " +
                          "WHERE pv.product_id = ?";
        List<ColorDto> colors = jdbcTemplate.query(colorSql, new Object[]{product.getId()}, (ResultSet rs, int rowNum) -> {
            ColorDto color = new ColorDto();
            color.setCode(rs.getString("code"));
            color.setName(rs.getString("name"));
            return color;
        });
        product.setColors(colors);

        // Fetch sizes
        String sizeSql = "SELECT DISTINCT pv.size FROM product_variant pv WHERE pv.product_id = ?";
        List<String> sizes = jdbcTemplate.query(sizeSql, new Object[]{product.getId()}, (ResultSet rs, int rowNum) -> rs.getString("size"));
        
        // make sizes in order
        List<String> sizeOrder = Arrays.asList("S", "M", "L", "XL", "XXL");

        sizes.sort((s1, s2) -> {
            int index1 = sizeOrder.indexOf(s1);
            int index2 = sizeOrder.indexOf(s2);
            return Integer.compare(index1, index2);
        });

        product.setSizes(sizes);

        // Fetch variants
        String variantSql = "SELECT pv.size, pv.color_code, pv.stock FROM product_variant pv WHERE pv.product_id = ?";
        List<VariantDto> variants = jdbcTemplate.query(variantSql, new Object[]{product.getId()}, (ResultSet rs, int rowNum) -> {
            VariantDto variant = new VariantDto();
            variant.setSize(rs.getString("size"));
            variant.setColorCode(rs.getString("color_code"));
            variant.setStock(rs.getInt("stock"));
            return variant;
        });
        product.setVariants(variants);

        // Fetch images
        String imageSql = "SELECT pi.image_path FROM product_image pi WHERE pi.product_id = ?";
        List<String> images = jdbcTemplate.query(imageSql, new Object[]{product.getId()}, (ResultSet rs, int rowNum) ->  elasticIp + "/" + storageLocation + "/" + rs.getString("image_path"));
        product.setImages(images);
    }

    private static class ProductRowMapper implements org.springframework.jdbc.core.RowMapper<ProductDto> {

        static private String storageLocation = "uploads";
        static private String elasticIp = "http://3.212.133.81";
        
        @Override
        public ProductDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProductDto product = new ProductDto();
            product.setId(rs.getInt("product_id"));
            product.setCategory(rs.getString("category"));
            product.setTitle(rs.getString("title"));
            product.setDescription(rs.getString("description"));
            product.setPrice(rs.getDouble("price"));
            product.setTexture(rs.getString("texture"));
            product.setWash(rs.getString("wash"));
            product.setPlace(rs.getString("place"));
            product.setNote(rs.getString("note"));
            product.setStory(rs.getString("story"));
            product.setMainImage( elasticIp + "/" + storageLocation + "/" + rs.getString("main_image"));
            return product;
        }
    }
}

