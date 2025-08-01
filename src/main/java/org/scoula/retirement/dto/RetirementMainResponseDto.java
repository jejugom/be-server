package org.scoula.retirement.dto;

import java.util.List;

import org.scoula.asset.dto.AssetStatusSummaryDto;
import org.scoula.product.dto.FundProductsDto;
import org.scoula.product.dto.GoldProductsDto;
import org.scoula.product.dto.MortgageLoanDto;
import org.scoula.product.dto.SavingsDepositsDto;
import org.scoula.product.dto.TimeDepositsDto;
import org.scoula.recommend.dto.CustomRecommendDto;
import org.scoula.user.dto.UserGraphDto;
import org.scoula.user.dto.UserInfoDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * 노후 메인 페이지에 응답할 모든 데이터를 담을 DTO
 * {자산현황}, {맞춤상품}, {전체금융상품}
 */
public class RetirementMainResponseDto {
	private UserGraphDto userInfo;
	private List<CustomRecommendDto> customRecommendPrdt; //맞춤상품
	private List<TimeDepositsDto> timeDeposits; //예금
	private List<SavingsDepositsDto> savingsDeposits; //적금
	private List<MortgageLoanDto> mortgageLoan; //주택담보대출
	private List<GoldProductsDto> goldProducts; //금상품
	private List<FundProductsDto> fundProducts; //펀드상품
}
