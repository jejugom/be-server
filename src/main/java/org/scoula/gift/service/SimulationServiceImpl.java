package org.scoula.gift.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.scoula.gift.domain.RecipientVo;
import org.scoula.gift.dto.RecipientGiftRequestDto;
import org.scoula.gift.dto.RecipientTaxDetailDto;
import org.scoula.gift.dto.SimulationRequestDto;
import org.scoula.gift.dto.SimulationResponseDto;
import org.scoula.gift.dto.AssetGiftRequestDto;
import org.scoula.gift.mapper.RecipientMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SimulationServiceImpl implements SimulationService {

	private final RecipientMapper recipientMapper;
	// --- Public Method (Controller's Entry Point) ---

	@Override
	public SimulationResponseDto runGiftTaxSimulation(SimulationRequestDto requestDto, String email) {
		// 1단계: 증여세 계산 (private 헬퍼 메서드 호출)
		TaxCalculationResult taxResult = calculateGiftTaxInternal(requestDto);

		// 2단계: 절세 전략 생성 (private 헬퍼 메서드 호출) - 향후 확장될 부분
		List<String> strategies = generateTaxSavingStrategies(requestDto, taxResult);

		// 3단계: 결과들을 조합하여 최종 응답 생성
		return new SimulationResponseDto(taxResult.getTotalEstimatedTax(), taxResult.getRecipientDetails(), strategies);
		// return new SimulationResponseDto(taxResult.getTotalEstimatedTax(), taxResult.getRecipientDetails());
	}

	// --- Private Helper Methods (향후 이 부분들의 로직을 구현) ---

	/**
	 * (Helper) 증여세 계산 로직
	 */
	private TaxCalculationResult calculateGiftTaxInternal(SimulationRequestDto requestDto) {
		long totalEstimatedTax = 0L;
		List<RecipientTaxDetailDto> recipientDetails = new ArrayList<>();

		for (RecipientGiftRequestDto giftRequest : requestDto.getSimulationList()) {
			// DB에서 수증자 정보 조회
			RecipientVo recipient = recipientMapper.findById(giftRequest.getRecipientId());
			if (recipient == null) {
				// 수증자 정보가 없으면 해당 건은 건너뜀 (또는 예외 처리)
				continue;
			}

			// --- 1단계: 수증자별 증여액 확정 ---
			// 1-1. 현재 시뮬레이션에서 증여할 금액 합산
			long currentGiftAmount = giftRequest.getCategoriesToGift().stream()
				.flatMap(category -> category.getAssets().stream())
				.mapToLong(AssetGiftRequestDto::getGiftAmount)
				.sum();

			// 1-2. 최근 10년 내 증여 이력 합산
			long priorGiftAmount = (recipient.getPriorGiftAmount() != null) ? recipient.getPriorGiftAmount() : 0L;
			long totalCumulativeGiftAmount = currentGiftAmount + priorGiftAmount;

			// --- 2단계: 수증자별 공제액 적용 ---
			long deductionAmount = getDeductionAmount(recipient);
			long taxableBase = Math.max(0, totalCumulativeGiftAmount - deductionAmount); // 과세표준

			// --- 3단계: 세율 적용 ---
			long calculatedTax = 0L;

            // 과세표준에 따른 세율 및 누진공제 적용
			if (taxableBase <= 100_000_000L) { // 1억원 이하
				calculatedTax = (long) (taxableBase * 0.10); // 세율 10%
			} else if (taxableBase <= 500_000_000L) { // 5억원 이하
				calculatedTax = (long) (taxableBase * 0.20) - 10_000_000L; // 세율 20%, 누진공제 1천만원
			} else if (taxableBase <= 1_000_000_000L) { // 10억원 이하
				calculatedTax = (long) (taxableBase * 0.30) - 60_000_000L; // 세율 30%, 누진공제 6천만원
			} else if (taxableBase <= 3_000_000_000L) { // 30억원 이하
				calculatedTax = (long) (taxableBase * 0.40) - 160_000_000L; // 세율 40%, 누진공제 1.6억원
			} else { // 30억원 초과
				calculatedTax = (long) (taxableBase * 0.50) - 460_000_000L; // 세율 50%, 누진공제 4.6억원
			}

            // 손자녀 할증 과세 (30% 할증)
			if ("손자녀".equals(recipient.getRelationship())) {
				calculatedTax = (long) (calculatedTax * 1.3);
			}

			// 수증자별 결과 리스트에 추가
			recipientDetails.add(new RecipientTaxDetailDto(
				recipient.getRecipientName(),
				currentGiftAmount, // 화면에는 이번에 증여하는 금액만 표시
				calculatedTax
			));

			totalEstimatedTax += calculatedTax;
		}
		return new TaxCalculationResult(totalEstimatedTax, recipientDetails);
	}

	/**
	 * (Helper) 수증자 정보에 따라 공제액을 반환합니다.
	 */
	private long getDeductionAmount(RecipientVo recipient) {
		if (recipient == null || recipient.getRelationship() == null) {
			return 0L;
		}

		switch (recipient.getRelationship()) {
			case "배우자":
				return 600_000_000L;
			case "자녀":
			case "손자녀":
				if (isMinor(recipient.getBirthDate())) {
					return 20_000_000L; // 미성년 자녀/손자녀 공제
				} else {
					return 50_000_000L; // 성년 자녀/손자녀 공제
				}
			case "기타친족":
			case "형제자매":
				return 10_000_000L;
			default:
				return 0L;
		}
	}

	/**
	 * (Helper) 생년월일을 기준으로 미성년자 여부(만 19세 미만)를 판단합니다.
	 */
	private boolean isMinor(Date birthDate) {
		if (birthDate == null) {
			return false; // 생년월일 정보가 없으면 성인으로 간주
		}
		LocalDate birthLocalDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return Period.between(birthLocalDate, LocalDate.now()).getYears() < 19;
	}

	// --------------------------------------------------------------------------------------------------------

	/**
	 * (Helper) 절세 전략 생성 로직.
	 * 지금은 비어있는 리스트를 반환합니다.
	 */
	private List<String> generateTaxSavingStrategies(SimulationRequestDto requestDto, TaxCalculationResult taxResult) {
		// TODO: 여기에 자산/수증자/세금계산 결과를 바탕으로 한
		//       룰 기반(Rule-based) 절세 전략 추천 로직을 구현합니다.

		// 현재는 빈 리스트 반환
		return Collections.emptyList();
	}

	/**
	 * private 메서드 간의 데이터 전달을 위한 내부 클래스 (혹은 record)
	 */
	@Getter
	@AllArgsConstructor
	private static class TaxCalculationResult {
		private final long totalEstimatedTax;
		private final List<RecipientTaxDetailDto> recipientDetails;
	}
}