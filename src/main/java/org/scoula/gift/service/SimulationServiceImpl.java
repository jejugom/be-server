package org.scoula.gift.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.scoula.gift.domain.RecipientVo;
import org.scoula.gift.domain.StrategyVo;
import org.scoula.gift.dto.*;
import org.scoula.gift.mapper.RecipientMapper;
import org.scoula.gift.mapper.StrategyMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 증여세 시뮬레이션 관련 비즈니스 로직을 처리하는 서비스 구현체
 */
@Service
@RequiredArgsConstructor
public class SimulationServiceImpl implements SimulationService {

	private final RecipientMapper recipientMapper;
	private final StrategyMapper strategyMapper;

	/**
	 * 증여세 시뮬레이션을 실행하고 세금 계산 결과와 절세 전략을 포함한 전체 결과를 반환합니다.
	 * 컨트롤러에서 호출되는 메인 진입점입니다.
	 * @param requestDto 시뮬레이션 요청 정보
	 * @return 계산된 세금 및 추천된 절세 전략이 포함된 응답 DTO
	 */
	@Override
	public SimulationResponseDto runGiftTaxSimulation(SimulationRequestDto requestDto, String email) {
		// 1. 세금 계산 및 전략 추천에 필요한 모든 정보를 담은 내부 결과 객체를 생성합니다.
		TaxCalculationResult taxResult = calculateGiftTaxInternal(requestDto);

		// 2. 위 결과 객체를 바탕으로 절세 전략을 추천합니다.
		List<String> strategies = generateTaxSavingStrategies(requestDto, taxResult);

		// 3. 최종 결과를 API 응답 DTO에 담아 반환합니다.
		return new SimulationResponseDto(
			taxResult.getTotalEstimatedTax(),
			taxResult.getRecipientTaxDetails(),
			strategies
		);
	}

	/**
	 * 실제 증여세 계산 로직을 수행하고, 절세 전략 추천에 필요한 중간 결과들을 반환합니다.
	 * @param requestDto 시뮬레이션 요청 정보
	 * @return 세금 계산 결과 및 전략 추천에 사용될 중간 데이터 묶음
	 */
	private TaxCalculationResult calculateGiftTaxInternal(SimulationRequestDto requestDto) {
		long totalCurrentGiftAmount = 0L;
		long totalEstimatedTax = 0L;
		List<RecipientTaxDetailDto> recipientTaxDetails = new ArrayList<>();
		List<RecipientVo> recipientsInSim = new ArrayList<>();

		for (RecipientGiftRequestDto giftRequest : requestDto.getSimulationList()) {
			RecipientVo recipient = recipientMapper.findById(giftRequest.getRecipientId());
			if (recipient == null) continue;
			recipientsInSim.add(recipient);

			long currentGiftAmount = giftRequest.getCategoriesToGift().stream()
				.flatMap(category -> category.getAssets().stream())
				.mapToLong(AssetGiftRequestDto::getGiftAmount)
				.sum();
			totalCurrentGiftAmount += currentGiftAmount;

			long priorGiftAmount = (recipient.getPriorGiftAmount() != null) ? recipient.getPriorGiftAmount() : 0L;
			long totalCumulativeGiftAmount = currentGiftAmount + priorGiftAmount;

			long deductionAmount = getDeductionAmount(recipient);
			long taxableBase = Math.max(0, totalCumulativeGiftAmount - deductionAmount);

			long calculatedTax = 0L;
			if (taxableBase <= 100_000_000L) calculatedTax = (long) (taxableBase * 0.10);
			else if (taxableBase <= 500_000_000L) calculatedTax = (long) (taxableBase * 0.20) - 10_000_000L;
			else if (taxableBase <= 1_000_000_000L) calculatedTax = (long) (taxableBase * 0.30) - 60_000_000L;
			else if (taxableBase <= 3_000_000_000L) calculatedTax = (long) (taxableBase * 0.40) - 160_000_000L;
			else calculatedTax = (long) (taxableBase * 0.50) - 460_000_000L;

			if ("손자녀".equals(recipient.getRelationship())) {
				calculatedTax = (long) (calculatedTax * 1.3);
			}

			recipientTaxDetails.add(new RecipientTaxDetailDto(recipient.getRecipientName(), currentGiftAmount, calculatedTax));
			totalEstimatedTax += calculatedTax;
		}
		return new TaxCalculationResult(totalEstimatedTax, recipientTaxDetails, recipientsInSim, totalCurrentGiftAmount);
	}

	/**
	 * 세금 계산 결과를 바탕으로 조건에 맞는 절세 전략을 생성합니다.
	 * @param requestDto 시뮬레이션 요청 정보
	 * @param taxResult 세금 계산 중간 결과
	 * @return 추천된 전략 메시지 리스트
	 */
	private List<String> generateTaxSavingStrategies(SimulationRequestDto requestDto, TaxCalculationResult taxResult) {
		List<String> recommendations = new ArrayList<>();
		List<StrategyVo> allRules = strategyMapper.findAll();
		Map<String, List<StrategyVo>> rulesByCategory = allRules.stream()
			.collect(Collectors.groupingBy(StrategyVo::getRuleCategory));

		// 카테고리별 규칙 검사 헬퍼 메서드 호출
		checkTotalAssetRules(recommendations, rulesByCategory.get("총 자산 규모"), taxResult);
		checkRecipientRules(recommendations, rulesByCategory.get("수증자 관계"), taxResult);
		checkGiftHistoryRules(recommendations, rulesByCategory.get("기존 증여이력"), taxResult);
		checkTaxPayerRules(recommendations, rulesByCategory.get("증여세 납부자"), taxResult);
		checkAssetTypeRules(recommendations, rulesByCategory.get("자산 유형"), requestDto);
		checkMaritalStatusRules(recommendations, rulesByCategory.get("수증자 결혼여부"), requestDto, taxResult);

		return recommendations;
	}

	// --- 카테고리별 전략 추천 헬퍼 메서드 ---

	private void checkTotalAssetRules(List<String> recommendations, List<StrategyVo> rules, TaxCalculationResult taxResult) {
		if (rules == null) return;
		long totalGiftAmount = taxResult.getTotalCurrentGiftAmount();

		for (StrategyVo rule : rules) {
			switch (rule.getStrategyCode()) {
				case "TOTAL_ASSET_GT_1B":
					if (totalGiftAmount >= 1_000_000_000L) recommendations.add(rule.getMessage());
					break;
				case "TOTAL_ASSET_GT_5B":
					if (totalGiftAmount >= 5_000_000_000L) recommendations.add(rule.getMessage());
					break;
			}
		}
	}

	private void checkRecipientRules(List<String> recommendations, List<StrategyVo> rules, TaxCalculationResult taxResult) {
		if (rules == null) return;
		List<RecipientVo> recipients = taxResult.getRecipientsInSim();
		boolean hasSpouse = recipients.stream().anyMatch(r -> "배우자".equals(r.getRelationship()));
		boolean hasGrandChild = recipients.stream().anyMatch(r -> "손자녀".equals(r.getRelationship()));
		boolean hasMinor = recipients.stream().anyMatch(r -> isMinor(r.getBirthDate()));

		for (StrategyVo rule : rules) {
			switch (rule.getStrategyCode()) {
				case "NO_SPOUSE_RECIPIENT":
					if (!hasSpouse) recommendations.add(rule.getMessage());
					break;
				case "HAS_GRANDCHILD_RECIPIENT":
					if (hasGrandChild) recommendations.add(rule.getMessage());
					break;
				case "HAS_MINOR_CHILD_RECIPIENT":
					if (hasMinor) recommendations.add(rule.getMessage());
					break;
			}
		}
	}

	private void checkGiftHistoryRules(List<String> recommendations, List<StrategyVo> rules, TaxCalculationResult taxResult) {
		if (rules == null) return;
		for (StrategyVo rule : rules) {
			switch(rule.getStrategyCode()){
				case "PRIOR_GIFT_EXISTS":
					if (taxResult.getRecipientsInSim().stream().anyMatch(r -> r.getHasPriorGift() != null && r.getHasPriorGift())) {
						recommendations.add(rule.getMessage());
					}
					break;
				case "CUMULATIVE_GIFT_EXCEEDS_DEDUCTION":
					if (taxResult.getRecipientsInSim().stream().anyMatch(r -> {
						long prior = (r.getPriorGiftAmount() != null) ? r.getPriorGiftAmount() : 0L;
						return prior > getDeductionAmount(r);
					})) {
						recommendations.add(rule.getMessage());
					}
					break;
			}
		}
	}

	private void checkTaxPayerRules(List<String> recommendations, List<StrategyVo> rules, TaxCalculationResult taxResult) {
		if (rules == null) return;
		boolean gifterPaysTax = taxResult.getRecipientsInSim().stream().anyMatch(r -> "증여자".equals(r.getGiftTaxPayer()));
		if (gifterPaysTax) {
			rules.stream().filter(r -> "GIFTER_PAYS_TAX".equals(r.getStrategyCode()))
				.findFirst().ifPresent(rule -> recommendations.add(rule.getMessage()));
		}
	}

	/**
	 * '자산 유형' 카테고리의 전략을 검사합니다.
	 * @param recommendations 추천 메시지를 담을 리스트
	 * @param rules '자산 유형' 카테고리에 해당하는 DB 규칙 리스트
	 * @param requestDto 증여 자산 유형을 확인하기 위한 원본 요청 DTO
	 */
	private void checkAssetTypeRules(List<String> recommendations, List<StrategyVo> rules, SimulationRequestDto requestDto) {
		if (rules == null) return;
		// 요청에 포함된 모든 자산 카테고리 코드를 중복 없이 수집
		Set<String> assetCategoryCodes = requestDto.getSimulationList().stream()
			.flatMap(r -> r.getCategoriesToGift().stream())
			.map(CategoryGiftRequestDto::getAssetCategoryCode)
			.collect(Collectors.toSet());

		// 아래 문자열('1', '2' 등)은 실제 사용하는 asset_category_code에 맞게 수정 필요
		boolean hasRealEstate = assetCategoryCodes.contains("1"); // '01'을 부동산으로 가정
		boolean hasBusiness = assetCategoryCodes.contains("4");   // '04'를 사업체/지분으로 가정
		boolean hasCash = assetCategoryCodes.contains("3");       // '03'을 현금/예금으로 가정

		for (StrategyVo rule : rules) {
			switch (rule.getStrategyCode()) {
				case "GIFT_BIZ_TO_SOLE_PROPRIETORSHIP":
				case "GIFT_BIZ_TO_CORPORATION":
					if (hasBusiness) recommendations.add(rule.getMessage());
					break;
				case "GIFT_REAL_ESTATE_APPRECIATION":
				case "GIFT_REAL_ESTATE_DEBT_SUCCESSION":
					if (hasRealEstate) recommendations.add(rule.getMessage());
					break;
				case "GIFT_CASH":
					if (hasCash) recommendations.add(rule.getMessage());
					break;
			}
		}
	}

	/**
	 * '수증자 결혼여부' 카테고리의 전략을 검사합니다.
	 * @param recommendations 추천 메시지를 담을 리스트
	 * @param rules '수증자 결혼여부' 카테고리에 해당하는 DB 규칙 리스트
	 * @param requestDto 증여 자산 유형을 확인하기 위한 원본 요청 DTO
	 * @param taxResult 수증자 정보를 확인하기 위한 중간 결과 객체
	 */
	private void checkMaritalStatusRules(List<String> recommendations, List<StrategyVo> rules, SimulationRequestDto requestDto, TaxCalculationResult taxResult) {
		if (rules == null) return;
		List<RecipientVo> recipients = taxResult.getRecipientsInSim();

		boolean hasUnmarriedChild = recipients.stream().anyMatch(r -> "자녀".equals(r.getRelationship()) && (r.getIsMarried() == null || !r.getIsMarried()));
		boolean hasMarriedChild = recipients.stream().anyMatch(r -> "자녀".equals(r.getRelationship()) && r.getIsMarried() != null && r.getIsMarried());
		boolean hasGrandChild = recipients.stream().anyMatch(r -> "손자녀".equals(r.getRelationship()));

		Set<String> assetCategoryCodes = requestDto.getSimulationList().stream()
			.flatMap(r -> r.getCategoriesToGift().stream()).map(CategoryGiftRequestDto::getAssetCategoryCode).collect(Collectors.toSet());
		boolean hasRealEstate = assetCategoryCodes.contains("01"); // '01'을 부동산으로 가정

		for (StrategyVo rule : rules) {
			switch (rule.getStrategyCode()) {
				case "HAS_UNMARRIED_CHILD_RECIPIENT":
					if (hasUnmarriedChild) recommendations.add(rule.getMessage());
					break;
				case "GIFT_REAL_ESTATE_TO_GRANDCHILD":
					if (hasRealEstate && hasGrandChild) recommendations.add(rule.getMessage());
					break;
				case "GIFT_HOUSE_TO_MARRIED_CHILD":
					if (hasRealEstate && hasMarriedChild) recommendations.add(rule.getMessage());
					break;
			}
		}
	}

	// --- 기존 유틸리티 헬퍼 메서드 ---
	private long getDeductionAmount(RecipientVo recipient) {
		if (recipient == null || recipient.getRelationship() == null) return 0L;
		switch (recipient.getRelationship()) {
			case "배우자": return 600_000_000L;
			case "자녀": case "손자녀": return isMinor(recipient.getBirthDate()) ? 20_000_000L : 50_000_000L;
			case "기타친족": case "형제자매": return 10_000_000L;
			default: return 0L;
		}
	}

	private boolean isMinor(Date birthDate) {
		if (birthDate == null) return false;
		return Period.between(birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()).getYears() < 19;
	}

	/**
	 * 세금 계산 및 전략 추천 로직 간 데이터 전달을 위한 내부 클래스
	 */
	@Getter
	@AllArgsConstructor
	private static class TaxCalculationResult {
		private final long totalEstimatedTax;
		private final List<RecipientTaxDetailDto> recipientTaxDetails;
		private final List<RecipientVo> recipientsInSim;
		private final long totalCurrentGiftAmount;
	}
}