package org.scoula.gift.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.scoula.asset.domain.AssetStatusVo;
import org.scoula.asset.mapper.AssetStatusMapper;
import org.scoula.gift.domain.RecipientVo;
import org.scoula.gift.domain.StrategyVo;
import org.scoula.gift.dto.AssetGiftRequestDto;
import org.scoula.gift.dto.CategoryGiftRequestDto;
import org.scoula.gift.dto.RecipientGiftRequestDto;
import org.scoula.gift.dto.RecipientTaxDetailDto;
import org.scoula.gift.dto.SimulationRequestDto;
import org.scoula.gift.dto.SimulationResponseDto;
import org.scoula.gift.mapper.RecipientMapper;
import org.scoula.gift.mapper.StrategyMapper;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 증여세 시뮬레이션 관련 비즈니스 로직을 처리하는 서비스 구현체
 */
@Service
@RequiredArgsConstructor
public class SimulationServiceImpl implements SimulationService {

	private final RecipientMapper recipientMapper;
	private final StrategyMapper strategyMapper;
	private final AssetStatusMapper assetStatusMapper;

	@Override
	public SimulationResponseDto runGiftTaxSimulation(SimulationRequestDto requestDto, String email) {
		TaxCalculationResult taxResult = calculateGiftTaxInternal(requestDto);
		List<String> strategies = generateTaxSavingStrategies(requestDto, taxResult);
		return new SimulationResponseDto(
			taxResult.getTotalEstimatedTax(),
			taxResult.getRecipientTaxDetails(),
			strategies
		);
	}

	private TaxCalculationResult calculateGiftTaxInternal(SimulationRequestDto requestDto) {
		long totalCurrentGiftAmount = 0L;
		long totalEstimatedTax = 0L;
		List<RecipientTaxDetailDto> recipientTaxDetails = new ArrayList<>();
		List<RecipientVo> recipientsInSim = new ArrayList<>();

		for (RecipientGiftRequestDto giftRequest : requestDto.getSimulationList()) {
			RecipientVo recipient = recipientMapper.findById(giftRequest.getRecipientId());
			if (recipient == null) {
				continue;
			}
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
			if (taxableBase <= 100_000_000L) {
				calculatedTax = (long)(taxableBase * 0.10);
			} else if (taxableBase <= 500_000_000L) {
				calculatedTax = (long)(taxableBase * 0.20) - 10_000_000L;
			} else if (taxableBase <= 1_000_000_000L) {
				calculatedTax = (long)(taxableBase * 0.30) - 60_000_000L;
			} else if (taxableBase <= 3_000_000_000L) {
				calculatedTax = (long)(taxableBase * 0.40) - 160_000_000L;
			} else {
				calculatedTax = (long)(taxableBase * 0.50) - 460_000_000L;
			}

			if ("손자녀".equals(recipient.getRelationship())) {
				calculatedTax = (long)(calculatedTax * 1.3);
			}

			recipientTaxDetails.add(
				new RecipientTaxDetailDto(recipient.getRecipientName(), currentGiftAmount, calculatedTax));
			totalEstimatedTax += calculatedTax;
		}
		return new TaxCalculationResult(totalEstimatedTax, recipientTaxDetails, recipientsInSim,
			totalCurrentGiftAmount);
	}

	private List<String> generateTaxSavingStrategies(SimulationRequestDto requestDto, TaxCalculationResult taxResult) {
		List<String> recommendations = new ArrayList<>();
		List<StrategyVo> allRules = strategyMapper.findAll();
		Map<String, List<StrategyVo>> rulesByCategory = allRules.stream()
			.collect(Collectors.groupingBy(StrategyVo::getRuleCategory));

		checkTotalAssetRules(recommendations, rulesByCategory.get("총 자산 규모"), taxResult);
		checkRecipientRules(recommendations, rulesByCategory.get("수증자 관계"), taxResult);
		checkGiftHistoryRules(recommendations, rulesByCategory.get("기존 증여이력"), taxResult);
		checkTaxPayerRules(recommendations, rulesByCategory.get("증여세 납부자"), taxResult);
		checkAssetTypeRules(recommendations, rulesByCategory.get("자산 유형"), requestDto);
		checkMaritalStatusRules(recommendations, rulesByCategory.get("수증자 결혼여부"), requestDto, taxResult);

		return recommendations;
	}

	private void checkTotalAssetRules(List<String> recommendations, List<StrategyVo> rules,
		TaxCalculationResult taxResult) {
		if (rules == null) {
			return;
		}
		long totalGiftAmount = taxResult.getTotalCurrentGiftAmount();
		for (StrategyVo rule : rules) {
			switch (rule.getStrategyCode()) {
				case "TOTAL_ASSET_GT_1B":
					if (totalGiftAmount >= 1_000_000_000L) {
						recommendations.add(rule.getMessage());
					}
					break;
				case "TOTAL_ASSET_GT_5B":
					if (totalGiftAmount >= 5_000_000_000L) {
						recommendations.add(rule.getMessage());
					}
					break;
			}
		}
	}

	private void checkRecipientRules(List<String> recommendations, List<StrategyVo> rules,
		TaxCalculationResult taxResult) {
		if (rules == null) {
			return;
		}
		List<RecipientVo> recipients = taxResult.getRecipientsInSim();
		boolean hasSpouse = recipients.stream().anyMatch(r -> "배우자".equals(r.getRelationship()));
		boolean hasGrandChild = recipients.stream().anyMatch(r -> "손자녀".equals(r.getRelationship()));
		boolean hasMinor = recipients.stream().anyMatch(r -> isMinor(r.getBirthDate()));

		for (StrategyVo rule : rules) {
			switch (rule.getStrategyCode()) {
				case "NOT_EXIST_SPOUSE": // 최종 코드로 수정
					if (!hasSpouse) {
						recommendations.add(rule.getMessage());
					}
					break;
				case "EXIST_GRANDCHILD": // 최종 코드로 수정
					if (hasGrandChild) {
						recommendations.add(rule.getMessage());
					}
					break;
				case "EXIST_MINOR_CHILD": // 최종 코드로 수정
					if (hasMinor) {
						recommendations.add(rule.getMessage());
					}
					break;
			}
		}
	}

	private void checkGiftHistoryRules(List<String> recommendations, List<StrategyVo> rules,
		TaxCalculationResult taxResult) {
		if (rules == null) {
			return;
		}
		for (StrategyVo rule : rules) {
			switch (rule.getStrategyCode()) {
				case "PRIOR_GIFT_EXISTS":
					if (taxResult.getRecipientsInSim()
						.stream()
						.anyMatch(r -> r.getHasPriorGift() != null && r.getHasPriorGift())) {
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

	private void checkTaxPayerRules(List<String> recommendations, List<StrategyVo> rules,
		TaxCalculationResult taxResult) {
		if (rules == null) {
			return;
		}
		boolean gifterPaysTax = taxResult.getRecipientsInSim()
			.stream()
			.anyMatch(r -> "본인".equals(r.getGiftTaxPayer()));
		if (gifterPaysTax) {
			rules.stream().filter(r -> "EXIST_GIFTER_PAYS_TAX".equals(r.getStrategyCode())) // 최종 코드로 수정
				.findFirst().ifPresent(rule -> recommendations.add(rule.getMessage()));
		}
	}

	private void checkAssetTypeRules(List<String> recommendations, List<StrategyVo> rules,
		SimulationRequestDto requestDto) {
		if (rules == null) {
			return;
		}

		// --- 1. 일반 자산 유형 확인 ---
		Set<String> assetCategoryCodes = requestDto.getSimulationList().stream()
			.flatMap(r -> r.getCategoriesToGift().stream())
			.map(CategoryGiftRequestDto::getAssetCategoryCode)
			.collect(Collectors.toSet());

		boolean hasRealEstate = assetCategoryCodes.contains("1");
		boolean hasCashOrSavings = assetCategoryCodes.contains("2") || assetCategoryCodes.contains("3");

		// --- 2. 사업체 자산 유형 상세 확인 ---
		// '사업체/지분'(코드:5) 자산의 ID만 모두 추출합니다.
		List<Integer> businessAssetIds = requestDto.getSimulationList().stream()
			.flatMap(r -> r.getCategoriesToGift().stream())
			.filter(c -> "5".equals(c.getAssetCategoryCode())) // 카테고리 코드가 '5'인 것만 필터링
			.flatMap(c -> c.getAssets().stream())
			.map(AssetGiftRequestDto::getAssetId)
			.collect(Collectors.toList());

		boolean isGiftingToSoleProprietorship = false;
		boolean isGiftingToCorporation = false;

		// 추출된 사업체 자산 ID가 있을 경우에만 DB를 조회하여 business_type을 확인합니다.
		if (!businessAssetIds.isEmpty()) {
			for (Integer assetId : businessAssetIds) {
				// AssetStatusMapper를 통해 자산의 상세 정보(business_type 포함)를 조회합니다.
				AssetStatusVo assetInfo = assetStatusMapper.findAssetStatusById(assetId);
				if (assetInfo != null && assetInfo.getBusinessType() != null) {
					if ("개인 사업자".equals(assetInfo.getBusinessType())) {
						isGiftingToSoleProprietorship = true;
					}
					if ("법인 사업자".equals(assetInfo.getBusinessType())) {
						isGiftingToCorporation = true;
					}
				}
			}
		}

		// --- 3. 최종 규칙 검사 ---
		for (StrategyVo rule : rules) {
			switch (rule.getStrategyCode()) {
				case "HAS_BIZ_TO_SOLE_PROPRIETORSHIP":
					// 개인 사업체를 증여하는 경우에만 이 전략을 추천합니다.
					if (isGiftingToSoleProprietorship) {
						recommendations.add(rule.getMessage());
					}
					break;
				case "HAS_BIZ_TO_CORPORATION":
					// 법인 사업자 경우에만 이 전략을 추천합니다.
					if (isGiftingToCorporation) {
						recommendations.add(rule.getMessage());
					}
					break;
				case "HAS_REAL_ESTATE_APPRECIATION":
				case "HAS_REAL_ESTATE_DEBT_SUCCESSION":
					if (hasRealEstate) {
						recommendations.add(rule.getMessage());
					}
					break;
				case "HAS_CASH":
					if (hasCashOrSavings) {
						recommendations.add(rule.getMessage());
					}
					break;
			}
		}
	}

	private void checkMaritalStatusRules(List<String> recommendations, List<StrategyVo> rules,
		SimulationRequestDto requestDto, TaxCalculationResult taxResult) {
		if (rules == null || rules.isEmpty()) {
			return;
		}
		List<RecipientVo> recipients = taxResult.getRecipientsInSim();
		boolean hasGrandChild = recipients.stream().anyMatch(r -> "손자녀".equals(r.getRelationship()));
		boolean hasUnmarriedChild = recipients.stream()
			.anyMatch(r -> "자녀".equals(r.getRelationship()) && (r.getIsMarried() == null || !r.getIsMarried()));
		boolean hasRealEstate = requestDto.getSimulationList()
			.stream()
			.flatMap(r -> r.getCategoriesToGift().stream())
			.anyMatch(cat -> "1".equals(cat.getAssetCategoryCode()));

		for (StrategyVo rule : rules) {
			switch (rule.getStrategyCode()) {
				case "EXIST_UNMARRIED_CHILD":
					if (hasUnmarriedChild) {
						recommendations.add(rule.getMessage());
					}
					break;
				case "HAS_GIFT_REAL_ESTATE_AND_EXIST_GRANDCHILD":
					if (hasRealEstate && hasGrandChild) {
						recommendations.add(rule.getMessage());
					}
					break;
				case "HAS_GIFT_REAL_ESTATE_AND_EXIST_UNMARRIED_CHILD":
					if (hasRealEstate && hasUnmarriedChild) {
						recommendations.add(rule.getMessage());
					}
					break;
			}
		}
	}

	private long getDeductionAmount(RecipientVo recipient) {
		if (recipient == null || recipient.getRelationship() == null) {
			return 0L;
		}
		return switch (recipient.getRelationship()) {
			case "배우자" -> 600_000_000L;
			case "자녀", "손자녀" -> isMinor(recipient.getBirthDate()) ? 20_000_000L : 50_000_000L;
			case "기타", "형제자매" -> 10_000_000L;
			default -> 0L;
		};
	}

	private boolean isMinor(Date birthDate) {
		if (birthDate == null) {
			return false;
		}
		return Period.between(birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now())
			.getYears() < 19;
	}

	@Getter
	@AllArgsConstructor
	private static class TaxCalculationResult {
		private final long totalEstimatedTax;
		private final List<RecipientTaxDetailDto> recipientTaxDetails;
		private final List<RecipientVo> recipientsInSim;
		private final long totalCurrentGiftAmount;
	}
}
