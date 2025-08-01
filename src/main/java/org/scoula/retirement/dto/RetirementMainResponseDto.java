package org.scoula.retirement.dto;

import java.util.List;

import org.scoula.product.dto.FundProductsDto;
import org.scoula.product.dto.GoldProductsDto;
import org.scoula.product.dto.MortgageLoanDto;
import org.scoula.product.dto.SavingsDepositsDto;
import org.scoula.product.dto.TimeDepositsDto;
import org.scoula.recommend.dto.CustomRecommendDto;
import org.scoula.user.dto.UserGraphDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "노후 메인 페이지 응답 DTO", description = "노후 메인 페이지의 모든 데이터를 담는 통합 DTO")
public class RetirementMainResponseDto {
	@ApiModelProperty(value = "사용자 정보 및 자산 현황 그래프 데이터")
	private UserGraphDto userInfo;

	@ApiModelProperty(value = "맞춤 추천 상품 목록")
	private List<CustomRecommendDto> customRecommendPrdt;

	@ApiModelProperty(value = "전체 예금 상품 목록")
	private List<TimeDepositsDto> timeDeposits;

	@ApiModelProperty(value = "전체 적금 상품 목록")
	private List<SavingsDepositsDto> savingsDeposits;

	@ApiModelProperty(value = "전체 주택담보대출 상품 목록")
	private List<MortgageLoanDto> mortgageLoan;

	@ApiModelProperty(value = "전체 금 상품 목록")
	private List<GoldProductsDto> goldProducts;

	@ApiModelProperty(value = "전체 펀드 상품 목록")
	private List<FundProductsDto> fundProducts;
}