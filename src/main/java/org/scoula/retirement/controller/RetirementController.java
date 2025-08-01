package org.scoula.retirement.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.scoula.asset.dto.AssetStatusSummaryDto;
import org.scoula.asset.service.AssetStatusService;
import org.scoula.product.mapper.ProductMapper;
import org.scoula.product.service.FundProductService;
import org.scoula.product.service.GoldProductService;
import org.scoula.product.service.MortgageLoanService;
import org.scoula.product.service.ProductsService; // ProductsService 임포트
import org.scoula.product.service.SavingDepositsService;
import org.scoula.product.service.TimeDepositsService;
import org.scoula.recommend.service.CustomRecommendService;
import org.scoula.retirement.dto.RetirementMainResponseDto; // RetirementMainResponseDTO 경로 확인
import org.scoula.user.dto.UserDto;
import org.scoula.user.dto.UserGraphDto;
import org.scoula.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/retirement")
public class RetirementController {

	private final UserService userServiceImpl;
	private final AssetStatusService assetStatusService;
	private final ProductsService productsService;
	private final CustomRecommendService customRecommendService;
	private final TimeDepositsService timeDepositsService;
	private final SavingDepositsService savingsService;
	private final MortgageLoanService mortgageLoansService;
	private final GoldProductService goldProductService;
	private final FundProductService fundProductService;
	private final ProductMapper productMapper;

	/**
	 * 노후 메인 페이지에 필요한 모든 데이터를 반환합니다.
	 * 자산 현황, 맞춤 상품, 전체 금융 상품 (예금, 적금, 주택담보대출)을 포함합니다.
	 * @return RetirementMainResponseDTO를 포함하는 ResponseEntity
	 */
	@GetMapping("")
	public ResponseEntity<RetirementMainResponseDto> getRetirementMainData(Authentication authentication) {
		RetirementMainResponseDto response = new RetirementMainResponseDto();
		String email = authentication.getName();

		// 0. 사용자 정보 조회
		UserDto userDto = userServiceImpl.getUser(email);

		// 1. 자산 현황 데이터 조회
		List<AssetStatusSummaryDto> assetList = assetStatusService.getAssetStatusSummaryByEmail(email);

		// 1-2. DTO 구조에 맞춰 UserGraphDto 생성 (핵심 수정 부분)
		UserGraphDto userGraphDto = UserGraphDto.builder()
			.userName(userDto.getUserName())
			.assetStatus(assetList)
			.build();

		// 수정된 DTO에 단일 객체로 설정
		response.setUserInfo(userGraphDto);

		// 2. 나머지 데이터 조회 및 설정 (기존과 동일)
		response.setCustomRecommendPrdt(customRecommendService.getCustomRecommendsByEmail(email));
		response.setTimeDeposits(productsService.getAllTimeDeposits());
		response.setSavingsDeposits(productsService.getAllSavingsDeposits());
		response.setMortgageLoan(productsService.getAllMortgageLoans());
		response.setGoldProducts(productsService.getAllGoldProducts());
		response.setFundProducts(productsService.getAllFundProducts());

		return ResponseEntity.ok(response);
	}

	/**
	 * 상품 상세 조회
	 */
	@GetMapping("/{finPrdtCd}")
	public ResponseEntity<?> getProductDetail(@PathVariable String finPrdtCd) {
		String prdt = productMapper.findNameByCode(finPrdtCd);
		String category = productMapper.findCategoryByFinPrdtCd(finPrdtCd);

		if (prdt == null) {
			throw new NoSuchElementException("상품을 찾을 수 없습니다: " + finPrdtCd);
		}

		switch (category) {
			case "1": //예금
				return ResponseEntity.ok(timeDepositsService.getDetail(finPrdtCd));
			case "2": //적금
				return ResponseEntity.ok(savingsService.getDetail(finPrdtCd));
			case "3": //주택담보대출
				return ResponseEntity.ok(mortgageLoansService.getDetail(finPrdtCd));
			case "4": //금
				return ResponseEntity.ok(goldProductService.getDetail(finPrdtCd));
			case "5": //펀드
				return ResponseEntity.ok(fundProductService.getDetail(finPrdtCd));
			default:
				throw new IllegalArgumentException("유효하지 않은 카테고리: " + category);
		}
	}
}
