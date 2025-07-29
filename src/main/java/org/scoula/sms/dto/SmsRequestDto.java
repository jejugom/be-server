package org.scoula.sms.dto;

import lombok.Data;

@Data
public class SmsRequestDto {
    // 수신자 전화번호
    // 현재는 프론트에서 받아오는데, 추후 백엔드 로그인 정보 참고로 수정?
    private String phoneNumber;
    
    // 예약 정보 관련 필드
    private String productName;
    private String branchName;
    private String reservationDate;
    private String reservationTime;

    // 아직 못받은 사용자 이름
    private String userName;
}