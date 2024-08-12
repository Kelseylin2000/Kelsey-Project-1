package com.example.stylish.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer saveOrderRequest(
            String shipping,
            String payment,
            BigDecimal subtotal,
            BigDecimal freight,
            BigDecimal total,
            String name,
            String phone,
            String email,
            String address,
            String time,
            int userId
    ) {
        String sql = "INSERT INTO order_request (shipping, payment, subtotal, freight, total, name, phone, email, address, time, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, new String[] {"order_id"});
                ps.setString(1, shipping);
                ps.setString(2, payment);
                ps.setBigDecimal(3, subtotal);
                ps.setBigDecimal(4, freight);
                ps.setBigDecimal(5, total);
                ps.setString(6, name);
                ps.setString(7, phone);
                ps.setString(8, email);
                ps.setString(9, address);
                ps.setString(10, time);
                ps.setInt(11, userId);
                return ps;
            }
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public void saveOrderItem(
            Integer orderId,
            Integer productId,
            String colorCode,
            String size,
            Integer quantity
    ) {
        String sql = "INSERT INTO order_item (order_id, product_id, color_code, size, quantity) VALUES (?, ?, ?, ?, ?)";
        
        jdbcTemplate.update(sql, 
            orderId,
            productId,
            colorCode,
            size,
            quantity
        );
    }

    public int getStock(Integer productId, String size, String colorCode) {
        String sql = "SELECT stock FROM product_variant WHERE product_id = ? AND size = ? AND color_code = ?";
        Integer stock = jdbcTemplate.queryForObject(sql, new Object[]{productId, size, colorCode}, Integer.class);
        return stock != null ? stock : 0;
    }

    public void updateStock(Integer productId, String size, String colorCode, int quantity) {
        String sql = "UPDATE product_variant SET stock = stock - ? WHERE product_id = ? AND size = ? AND color_code = ?";
        jdbcTemplate.update(sql, quantity, productId, size, colorCode);
    }
}
