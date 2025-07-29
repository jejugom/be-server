package org.scoula.sms.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.sms.dto.SmsRequestDto;
import org.scoula.sms.dto.SmsResponseDto;
import org.scoula.sms.service.SmsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sms") // 기본 API 경로
@RequiredArgsConstructor
@Log4j2
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SmsController {

    private final SmsService smsService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("SMS API 연결 성공!");
    }

    @PostMapping("/send")
    public ResponseEntity<SmsResponseDto> sendSms(@RequestBody SmsRequestDto request) {
        log.info("SMS 발송 요청: " + request);
        // 단일 SMS 발송
        SmsResponseDto response = smsService.sendSms(request);
        
        return ResponseEntity.ok(response);
    }

    // 추후 추가 가능한 예약 전송...
    @PostMapping("/reservation")
    public ResponseEntity<SmsResponseDto> sendReservationSms(@RequestBody SmsRequestDto request) {
        log.info("예약 SMS 발송 요청: " + request);
        
        // 예약 정보를 기반으로 SMS 전송
        SmsResponseDto response = smsService.sendSms(request);
        
        return ResponseEntity.ok(response);
    }
}