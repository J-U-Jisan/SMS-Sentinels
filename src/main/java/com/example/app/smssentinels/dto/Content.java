package com.example.app.smssentinels.dto;

import lombok.Data;

@Data
public class Content {
    private String transactionId;
    private String operator;
    private String shortCode;
    private String msisdn;
    private String sms;
}
