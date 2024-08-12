package com.example.stylish.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {
    private String prime;
    
    @JsonProperty("partner_key")
    private String partnerKey;

    @JsonProperty("merchant_id")
    private String merchantId;

    private String details;
    private BigDecimal amount;
    private Cardholder cardholder;
    private boolean remember;
}