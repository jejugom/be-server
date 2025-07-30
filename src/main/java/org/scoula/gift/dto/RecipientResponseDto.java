package org.scoula.gift.dto;

import java.time.LocalDate;
import org.scoula.gift.domain.RecipientVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipientResponseDto {
	private Integer recipientId;
	private String email;
	private String relationship;
	private String recipientName;
	private LocalDate birthDate;      // 생년월일 (java.time.LocalDate)
	private Boolean isMarried;
	private Boolean hasPriorGift;
	private Long priorGiftAmount;
	private String giftTaxPayer;

	/**
	 * VO를 DTO로 변환하는 정적 팩토리 메서드.
	 * @param vo RecipientVo 객체
	 * @return RecipientResponseDto 객체
	 */
	public static RecipientResponseDto from(RecipientVo vo) {
		return new RecipientResponseDto(
			vo.getRecipientId(),
			vo.getEmail(),
			vo.getRelationship(),
			vo.getRecipientName(),
			vo.getBirthDate(),
			vo.getIsMarried(),
			vo.getHasPriorGift(),
			vo.getPriorGiftAmount(),
			vo.getGiftTaxPayer()
		);
	}
}