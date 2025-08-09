package org.scoula.retirement.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.scoula.asset.dto.AssetStatusSummaryDto;
import org.scoula.asset.service.AssetStatusService;
import org.scoula.news.service.NewsService;
import org.scoula.product.domain.ProductVo;
import org.scoula.product.dto.FundDailyReturnDto;
import org.scoula.product.dto.ProductDto;
import org.scoula.product.mapper.ProductMapper;
import org.scoula.product.service.ProductService;
import org.scoula.recommend.service.CustomRecommendService;
import org.scoula.retirement.dto.RetirementMainResponseDto;
import org.scoula.user.dto.UserDto;
import org.scoula.user.dto.UserGraphDto;
import org.scoula.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@Api(tags = "노후 관리 페이지 API", description = "노후 메인 페이지 데이터 조회 및 상품 상세 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/retirement")
public class RetirementController {

	private final UserService userServiceImpl;
	private final AssetStatusService assetStatusService;
	private final ProductService productService;
	private final CustomRecommendService customRecommendService;
	private final ProductMapper productMapper;
	private final NewsService newsService;

	@ApiOperation(value = "노후 메인 페이지 데이터 조회", notes = "현재 로그인한 사용자의 노후 메인 페이지에 필요한 모든 데이터를 조회합니다.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "조회 성공"),
		@ApiResponse(code = 401, message = "인증되지 않은 사용자")
	})
	@GetMapping("")
	public ResponseEntity<RetirementMainResponseDto> getRetirementMainData(Authentication authentication) {
		// RetirementMainResponseDto response = new RetirementMainResponseDto();
		String email = authentication.getName();

		// 0. 사용자 정보 조회
		UserDto userDto = userServiceImpl.getUser(email);

		// 1. 자산 현황 데이터 조회
		List<AssetStatusSummaryDto> assetList = assetStatusService.getAssetStatusSummaryByEmail(email);

		// 1-2. DTO 구조에 맞춰 UserGraphDto 생성
		UserGraphDto userGraphDto = UserGraphDto.builder()
			.userName(userDto.getUserName())
			.assetStatus(assetList)
			.build();
		// response.setUserInfo(userGraphDto);

		// 2. 나머지 데이터 조회 및 설정
		Map<String, List<? extends ProductDto>> allProducts = productService.findAllProducts();

		// 응답 DTO 생성
		RetirementMainResponseDto response = RetirementMainResponseDto.builder()
			.userInfo(userGraphDto)
			.allProducts(allProducts)
			.customRecommendPrdt(customRecommendService.getCustomRecommendsByEmail(email))
			.news(newsService.getAllNews())
			.build();

		return ResponseEntity.ok(response);
	}

	@ApiOperation(value = "금융 상품 상세 조회", notes = "금융 상품 코드로 특정 상품의 상세 정보를 조회합니다.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "조회 성공"),
		@ApiResponse(code = 404, message = "존재하지 않는 상품 코드"),
		@ApiResponse(code = 400, message = "유효하지 않은 상품 카테고리")
	})
	@GetMapping("/{finPrdtCd}")
	public ResponseEntity<?> getProductDetail(@PathVariable String finPrdtCd) {
		ProductVo productVo = productService.getProductDetail(finPrdtCd);
		Map<String, Object> response = new HashMap<>();
		response.put("product",productVo);
		//펀드 상품일 경우 응답 형식에 3개월 수익률도 추가
		if (finPrdtCd.matches("^[123].*")) { // startsWith 3번 대신 정규식
			List<FundDailyReturnDto> fundDailyReturnDtos =
				productService.getFundDailyReturnByCode(finPrdtCd)
					.stream()
					.map(FundDailyReturnDto::of) // 여기서 바로 변환
					.collect(Collectors.toList());
			response.put("fundReturn",fundDailyReturnDtos);
		}
		return ResponseEntity.ok(response);
	}
}
