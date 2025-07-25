package org.scoula.sms.util;

import lombok.extern.log4j.Log4j;
import org.scoula.sms.dto.SmsRequestDto;
import org.scoula.sms.dto.SmsResponseDto;
import org.springframework.stereotype.Component;

@Component
@Log4j
public class SmsApiClient {

    // TODO: 외부 API 설정값들을 properties에서 주입받도록 수정 예정
    private static final String API_URL = ""; // 외부 API URL
    private static final String API_KEY = ""; // API 키
    
    public SmsResponseDto sendMessage(SmsRequestDto request) {
        log.info("외부 SMS API 호출 시작");
        
        // TODO: 실제 외부 API 호출 로직 구현
        // 1. HTTP 클라이언트로 외부 API 호출
        // 2. 응답 데이터 파싱
        // 3. SmsResponseDto로 변환하여 반환
        
        // 임시 응답 (실제 구현 시 제거)
        SmsResponseDto response = new SmsResponseDto();
        response.setSuccess(true);
        response.setMessage("SMS 발송 완료");
        response.setMessageId("temp_message_id");
        
        return response;
    }
}