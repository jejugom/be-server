package org.scoula.gift.dto;

import java.util.Date;

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
	private Date birthDate;           // 생년월일 (java.util.Date)
	private Boolean isMarried;
	private Boolean hasPriorGift;
	private Long priorGiftAmount;
	private String giftTaxPayer;

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