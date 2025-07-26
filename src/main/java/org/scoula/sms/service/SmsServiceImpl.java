package org.scoula.sms.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.scoula.sms.dto.SmsRequestDto;
import org.scoula.sms.dto.SmsResponseDto;
import org.scoula.sms.util.SmsApiClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j
public class SmsServiceImpl implements SmsService {

    private final SmsApiClient smsApiClient;

    @Override
    public SmsResponseDto sendSms(SmsRequestDto request) {
        log.info("SMS 발송 서비스 시작: " + request.getPhoneNumber());
        
        try {
            return smsApiClient.sendMessage(request);
        } catch (Exception e) {
            log.error("SMS 발송 실패: " + e.getMessage(), e);
            
            SmsResponseDto errorResponse = new SmsResponseDto();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("SMS 발송에 실패했습니다.");
            errorResponse.setErrorCode("SEND_FAILED");
            
            return errorResponse;
        }
    }
}