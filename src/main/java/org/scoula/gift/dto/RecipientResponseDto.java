package org.scoula.gift.dto;

import java.util.Date;

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
	private Date birthDate;           // 생년월일 (java.util.Date)
	private Boolean isMarried;
	private Boolean hasPriorGift;
	private Long priorGiftAmount;
	private String giftTaxPayer;

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
