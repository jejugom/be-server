package org.scoula.gift.domain;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "RecipientVo", description = "DB의 recipient 테이블과 매핑되는 핵심 객체")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipientVo {
	private Integer recipientId;
	private String email;
	private String relationship;
	private String recipientName;
	private Date birthDate;
	private Boolean isMarried;
	private Boolean hasPriorGift;
	private Long priorGiftAmount;
	private String giftTaxPayer;
}
