package org.scoula.gift.dto;

import java.util.Date;

import org.scoula.gift.domain.RecipientVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "RecipientResponseDto", description = "수증자 정보 조회를 위한 응답 DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipientResponseDto {

	@ApiModelProperty(value = "수증자 ID (PK)", example = "1")
	private Integer recipientId;

	@ApiModelProperty(value = "사용자 이메일 (FK)", example = "user@example.com")
	private String email;

	@ApiModelProperty(value = "관계", example = "자녀")
	private String relationship;

	@ApiModelProperty(value = "수증자 이름", example = "홍길동")
	private String recipientName;

	@ApiModelProperty(value = "생년월일", example = "2000-01-01")
	private Date birthDate;

	@ApiModelProperty(value = "결혼 여부", example = "false")
	private Boolean isMarried;

	@ApiModelProperty(value = "사전 증여 여부", example = "true")
	private Boolean hasPriorGift;

	@ApiModelProperty(value = "사전 증여 가액", example = "10000000")
	private Long priorGiftAmount;

	@ApiModelProperty(value = "증여세 납부자", example = "수증자")
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
