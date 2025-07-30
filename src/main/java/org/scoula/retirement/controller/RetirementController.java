package org.scoula.retirement.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.scoula.asset.dto.AssetStatusSummaryDto;
import org.scoula.asset.service.AssetStatusService;
import org.scoula.product.mapper.ProductMapper;
import org.scoula.product.service.MortgageLoanService;
import org.scoula.product.service.ProductsService; // ProductsService 임포트
import org.scoula.product.service.SavingDepositsService;
import org.scoula.product.service.TimeDepositsService;
import org.scoula.retirement.dto.RetirementMainResponseDto; // RetirementMainResponseDTO 경로 확인
import org.scoula.user.dto.UserDto;
import org.scoula.user.dto.UserInfoDto;
import org.scoula.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/retirement")
public class RetirementController {

	private final UserService userServiceImpl;
	private final AssetStatusService assetStatusService;
	private final ProductsService productsService;
	// private CustomRecommendServiceImpl customRecommendService;
	private final TimeDepositsService timeDepositsService;
	private final SavingDepositsService savingsService;
	private final MortgageLoanService mortgageLoansService;
	private final ProductMapper productMapper;

	/**
	 * 노후 메인 페이지에 필요한 모든 데이터를 반환합니다.
	 * 자산 현황, 맞춤 상품, 전체 금융 상품 (예금, 적금, 주택담보대출)을 포함합니다.
	 * @return RetirementMainResponseDTO를 포함하는 ResponseEntity
	 */
	@GetMapping("/main")
	public ResponseEntity<RetirementMainResponseDto> getRetirementMainData(@RequestParam String email) {
		RetirementMainResponseDto response = new RetirementMainResponseDto();

		// 0. 사용자 정보 조회
		UserDto userDto = userServiceImpl.getUser(email);
		UserInfoDto userInfoDto = UserInfoDto.of(userDto.toVo());

		// 1. 자산 현황 데이터 조회 및 설정
		List<AssetStatusSummaryDto> assetList = assetStatusService.getAssetStatusSummaryByEmail(email);

		// 1-2. DTO 구조에 맞춰 user_info 세팅
		RetirementMainResponseDto.UserInfo userInfo = RetirementMainResponseDto.UserInfo.builder()
			.user_name(userInfoDto)
			.asset_status(assetList)
			.build();

		response.setUser_info(List.of(userInfo));

		// 2. 맞춤 상품 데이터 조회 및 설정 (CustomRecommendDto 관련 서비스 필요)
		// response.setCustomRecommend_prdt(customRecommendService.getCustomRecommendsByEmail(email));

		// 3. ProductService를 통해 각 상품 데이터 조회 및 설정
		response.setTimeDeposits(productsService.getAllTimeDeposits());
		response.setSavingDeposits(productsService.getAllSavingsDeposits());
		response.setMortgageLoan(productsService.getAllMortgageLoans());


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
			default:
				throw new IllegalArgumentException("유효하지 않은 카테고리: " + category);
		}
	}
}
