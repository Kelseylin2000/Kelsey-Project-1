package com.example.stylish.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "product_id", "picture", "story" })
public class CampaignDto {
    
    @JsonProperty("product_id")
    private int productId;

    private String picture;
    private String story;
}
