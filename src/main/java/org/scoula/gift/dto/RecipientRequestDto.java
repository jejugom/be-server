package org.scoula.gift.dto;

import java.time.LocalDate;
import org.scoula.gift.domain.RecipientVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipientRequestDto {

	private String relationship;
	private String recipientName;
	private LocalDate birthDate;      // 생년월일 (java.time.LocalDate)
	private Boolean isMarried;
	private Boolean hasPriorGift;
	private Long priorGiftAmount;
	private String giftTaxPayer;

	/**
	 * DTO를 VO로 변환하는 메서드.
	 * @param email 현재 로그인한 사용자의 이메일
	 * @return RecipientVo 객체
	 */
	public RecipientVo toVo(String email) {
		return new RecipientVo(
			null, // recipientId는 DB에서 생성되므로 null
			email,
			this.relationship,
			this.recipientName,
			this.birthDate,
			this.isMarried,
			this.hasPriorGift,
			this.priorGiftAmount,
			this.giftTaxPayer
		);
	}
}