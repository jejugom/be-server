package org.scoula.sms.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.scoula.sms.dto.SmsRequestDto;
import org.scoula.sms.dto.SmsResponseDto;
import org.scoula.sms.service.SmsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sms")
@RequiredArgsConstructor
@Log4j
public class SmsController {

    private final SmsService smsService;

    @PostMapping("/send")
    public ResponseEntity<SmsResponseDto> sendSms(@RequestBody SmsRequestDto request) {
        log.info("SMS 발송 요청: " + request);
        
        SmsResponseDto response = smsService.sendSms(request);
        
        return ResponseEntity.ok(response);
    }
}