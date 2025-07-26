package org.scoula.sms.dto;

import lombok.Data;

@Data
public class SmsResponseDto {
    private boolean success;
    private String message;
    private String messageId;
    private String errorCode;
}