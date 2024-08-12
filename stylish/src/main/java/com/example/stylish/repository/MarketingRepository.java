package com.example.stylish.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.stylish.dto.CampaignDto;

@Repository
public class MarketingRepository {

    private JdbcTemplate jdbcTemplate;

    @Value("${storage.location}")
    private String storageLocation;

    @Value("${elastic.ip}")
    private String elasticIp;

    public MarketingRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveCampaign(int productId, String imagePath, String story) {
        String sql = "INSERT INTO campaign (product_id, image_path, story) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, productId, imagePath, story);
    }

    public List<CampaignDto> getCampaignList() {
        String sql = "SELECT product_id, image_path, story FROM campaign";

        return jdbcTemplate.query(sql, new RowMapper<CampaignDto>() {
            @Override
            public CampaignDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new CampaignDto(
                        rs.getInt("product_id"),
                        elasticIp + "/" + storageLocation + "/" + rs.getString("image_path"),
                        rs.getString("story")
                );
            }
        });
    }

}
