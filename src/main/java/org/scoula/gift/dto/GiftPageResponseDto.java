package org.scoula.gift.dto;

import java.util.List;

import org.scoula.asset.dto.AssetStatusSummaryDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "증여 페이지 데이터 응답 DTO", description = "증여 페이지에 필요한 전체 데이터를 담는 최종 응답 데이터")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftPageResponseDto {
	@ApiModelProperty(value = "수증자 목록 정보")
	private List<RecipientResponseDto> recipients;

	@ApiModelProperty(value = "자산 현황 요약 정보")
	private List<AssetStatusSummaryDto> assetSummary;
}
