package com.example.stylish.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.stylish.dto.PaymentRequestDto;
import com.example.stylish.dto.TapPayResponseDto;

import com.example.stylish.exception.InternalServerErrorException;


@Service
public class TapPayService {

    @Value("${tappay.partner.key}")
    private String partnerKey;

    @Value("${tappay.merchant.id}")
    private String merchantId;

    @Value("${tappay.api.url}")
    private String tapPayApiUrl;

    public ResponseEntity<TapPayResponseDto> sendPaymentRequest(PaymentRequestDto paymentRequest) {
        RestTemplate restTemplate = new RestTemplate(new SimpleClientHttpRequestFactory());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("x-api-key", partnerKey);

        HttpEntity<PaymentRequestDto> entity = new HttpEntity<>(paymentRequest, headers);
        ResponseEntity<TapPayResponseDto> response;

        try{
        response = restTemplate.exchange(tapPayApiUrl, HttpMethod.POST, entity, TapPayResponseDto.class);
        }catch(Exception e){
            throw new InternalServerErrorException("Payment request to TapPay failed");
        }

        return response;
    }
}
