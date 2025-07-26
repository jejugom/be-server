package org.scoula.sms.service;

import org.scoula.sms.dto.SmsRequestDto;
import org.scoula.sms.dto.SmsResponseDto;

public interface SmsService {
    SmsResponseDto sendSms(SmsRequestDto request);
}