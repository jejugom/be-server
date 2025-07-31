package org.scoula.gift.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "RecipientIdResponseDto", description = "수증자 생성 후 ID만 반환하기 위한 응답 DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipientIdResponseDto {
	@ApiModelProperty(value = "생성된 수증자 ID", example = "17")
	private Integer recipientId;
}
