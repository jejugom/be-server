package org.scoula.gift.domain;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RecipientVo: 데이터베이스의 'recipient' 테이블과 1:1로 매핑되는 핵심 VO(Value Object).
 * Service 계층과 Mapper 계층에서 주로 사용됩니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipientVo {
	private Integer recipientId;      // 수증자 ID (PK, Auto Increment)
	private String email;             // 사용자 이메일 (FK)
	private String relationship;      // 관계
	private String recipientName;     // 수증자 이름
	private LocalDate birthDate;      // 생년월일
	private Boolean isMarried;        // 결혼 여부
	private Boolean hasPriorGift;     // 사전 증여 여부
	private Long priorGiftAmount;     // 사전 증여 가액
	private String giftTaxPayer;      // 증여세 납부자
}