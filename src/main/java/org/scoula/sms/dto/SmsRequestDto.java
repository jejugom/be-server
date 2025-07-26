package org.scoula.sms.dto;

import lombok.Data;

@Data
public class SmsRequestDto {
    private String phoneNumber;
    private String message;
    private String senderNumber;
}