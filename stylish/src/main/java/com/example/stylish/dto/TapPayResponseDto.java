package com.example.stylish.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TapPayResponseDto {

    @JsonProperty("auth_code")
    private String authCode;

    @JsonProperty("bank_result_code")
    private int bankResultCode;

    @JsonProperty("bank_result_msg")
    private String bankResultMsg;

    private int status;

    private String msg;

    @JsonProperty("rec_trade_id")
    private String recTradeId;

    @JsonProperty("bank_transaction_id")
    private String bankTransactionId;

    private int amount;

    @JsonProperty("order_number")
    private String orderNumber;

    private String acquirer;

    @JsonProperty("transaction_time_millis")
    private long transactionTimeMillis;

    @JsonProperty("payment_url")
    private String paymentUrl;
}
