package com.example.stylish.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.stylish.dto.Cardholder;
import com.example.stylish.dto.ColorDto;
import com.example.stylish.dto.DataResponseDto;
import com.example.stylish.dto.OrderDto;
import com.example.stylish.dto.OrderItemDto;
import com.example.stylish.dto.OrderNumberDto;
import com.example.stylish.dto.OrderRequestDto;
import com.example.stylish.dto.PaymentRequestDto;
import com.example.stylish.dto.RecipientDto;
import com.example.stylish.dto.TapPayResponseDto;
import com.example.stylish.dto.UserDto;
import com.example.stylish.exception.BadRequestException;
import com.example.stylish.repository.OrderRepository;
import com.example.stylish.security.JwtUtil;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

    @Value("${tappay.partner.key}")
    private String partnerKey;

    @Value("${tappay.merchant.id}")
    private String merchantId;

    @Value("${tappay.merchant.id}")
    private String tapPayApiUrl;

    private OrderRepository orderRepository;
    private TapPayService tapPayService;
    private JwtUtil jwtUtil;

    public OrderService(OrderRepository orderRepository, TapPayService tapPayService, JwtUtil jwtUtil){
        this.orderRepository = orderRepository;
        this.tapPayService = tapPayService;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public DataResponseDto<OrderNumberDto> checkout(OrderRequestDto orderRequest, String token){

        // Step 1: Check stock and deduct the quantity.
        OrderDto order = orderRequest.getOrder();
        List<OrderItemDto> orderItems = order.getList();

        for (OrderItemDto item : orderItems) {

            Integer productId = item.getId();
            String size = item.getSize();
            String colorCode = item.getColor().getCode();
            int quantity = item.getQty();

            // Check stock
            int stock = 0;
            try{
                stock = orderRepository.getStock(productId, size, colorCode);
            }catch(EmptyResultDataAccessException e){
                throw new BadRequestException("product "+ item.getName() + " or the variant does not exist");
            }

            if (stock < quantity) {
                throw new BadRequestException("Insufficient stock for product " + item.getName());
            }

            // Deduct stock
            orderRepository.updateStock(productId, size, colorCode, quantity);
        }

        // step 2: send paymentRequest
        RecipientDto recipient = order.getRecipient();
        String details = "";
        for (OrderItemDto item : orderItems) {
            details += item.getName();
            details += ",";
        }
        if (details.length() > 0) {
            details.substring(0, details.length() - 1); 
        }
        
        Cardholder cardholder = new Cardholder(
            recipient.getPhone(), 
            recipient.getName(), 
            recipient.getEmail()
        );

        PaymentRequestDto paymentRequest = new PaymentRequestDto(
            orderRequest.getPrime(), 
            partnerKey, merchantId, 
            details, 
            order.getTotal(), 
            cardholder, 
            true
        );

        ResponseEntity<TapPayResponseDto> response = tapPayService.sendPaymentRequest(paymentRequest);

        // step 3: check if the payment successes
        TapPayResponseDto responseBody = response.getBody();
        if(response.getStatusCode() != HttpStatus.OK || responseBody == null || responseBody.getStatus() != 0){
            throw new BadRequestException("Payment failed");
        }

        // step 4: save data
        String jwt = token.substring(7);
        UserDto user = jwtUtil.getUserDtoFromToken(jwt);

        if(user == null){
            throw new BadRequestException("wrong token");
        }
            
        Integer orderId = orderRepository.saveOrderRequest(
            order.getShipping(),
            order.getPayment(),
            order.getSubtotal(),
            order.getFreight(),
            order.getTotal(),
            recipient.getName(),
            recipient.getPhone(),
            recipient.getEmail(),
            recipient.getAddress(),
            recipient.getTime(),
            user.getId()
        );

        for (OrderItemDto item : orderItems) {
            ColorDto color = item.getColor();

            orderRepository.saveOrderItem(
                orderId,
                item.getId(),
                color.getCode(),
                item.getSize(),
                item.getQty()
            );
        }

        OrderNumberDto orderNumber = new OrderNumberDto(orderId);
        return new DataResponseDto<>(orderNumber);
    }
}
