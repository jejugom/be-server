package org.scoula.product.struct;

import java.util.List;
import java.util.stream.Collectors;

import org.scoula.product.domain.DepositVo;
import org.scoula.product.domain.FundVo;
import org.scoula.product.domain.GoldVo;
import org.scoula.product.domain.MortgageVo;
import org.scoula.product.domain.SavingVo;
import org.scoula.product.domain.TrustVo;
import org.scoula.product.dto.DepositDto;
import org.scoula.product.dto.DepositOptionDto;
import org.scoula.product.dto.FundDto;
import org.scoula.product.dto.FundOptionDto;
import org.scoula.product.dto.GoldDto;
import org.scoula.product.dto.MortgageDto;
import org.scoula.product.dto.MortgageOptionDto;
import org.scoula.product.dto.SavingDto;
import org.scoula.product.dto.SavingOptionDto;
import org.scoula.product.dto.TrustDto;

public class ProductVoToMapper {
	/**
	 * 예금 VO를 DTO로 변환
	 * @param vo
	 * @return
	 */
	public static DepositDto toDepositDto(DepositVo vo) {
		List<DepositOptionDto> depositDtoList = vo.getOptionList().stream()
			.map(option -> DepositOptionDto.builder()
				.saveTrm(option.getSaveTrm())
				.intrRateTypeNm(option.getIntrRateTypeNm())
				.intrRate(option.getIntrRate())
				.intrRate2(option.getIntrRate2())
				.build())
			.collect(Collectors.toList());

		return DepositDto.builder()
			// 공통 정보 -부모
			.finPrdtCd(vo.getFinPrdtCd())
			.finPrdtNm(vo.getFinPrdtNm())
			.prdtFeature(vo.getPrdtFeature())
			// 공통 정보 -중간부모
			.korCoNm(vo.getKorCoNm())
			.finPrdtCategory(vo.getFinPrdtCategory())
			.description(vo.getDescription())
			.joinWay(vo.getJoinWay())
			.recReason(vo.getRecReason())

			// 예금 전용 정보
			.mtrtInt(vo.getMtrtInt())
			.spclCnd(vo.getSpclCnd())
			.joinDeny(vo.getJoinDeny())
			.joinMember(vo.getJoinMember())
			.etcNote(vo.getEtcNote())
			// .tendency(vo.getTendency())

			// 옵션 리스트
			.optionList(depositDtoList)
			.build();
	}

	/**
	 * 적금 VO를 DTO로 변환
	 * @param vo
	 * @return
	 */
	public static SavingDto toSavingDto(SavingVo vo) {
		List<SavingOptionDto> savingDtoList = vo.getOptionList().stream()
			.map(option -> SavingOptionDto.builder()
				.saveTrm(option.getSaveTrm())
				.rsrvTypeNm(option.getRsrvTypeNm())
				.intrRateTypeNm(option.getIntrRateTypeNm())
				.intrRate(option.getIntrRate())
				.intrRate2(option.getIntrRate2())
				.build())
			.collect(Collectors.toList());

		return SavingDto.builder()
			// 공통 정보 -부모
			.finPrdtCd(vo.getFinPrdtCd())
			.finPrdtNm(vo.getFinPrdtNm())
			.prdtFeature(vo.getPrdtFeature())
			
			// 공통 정보 -중간부모
			.korCoNm(vo.getKorCoNm())
			.finPrdtCategory(vo.getFinPrdtCategory())
			.description(vo.getDescription())
			.joinWay(vo.getJoinWay())
			.recReason(vo.getRecReason())

			// 적금 전용 정보
			.mtrtInt(vo.getMtrtInt())
			.spclCnd(vo.getSpclCnd())
			.joinDeny(vo.getJoinDeny())
			.joinMember(vo.getJoinMember())
			.etcNote(vo.getEtcNote())
			.maxLimit(vo.getMaxLimit())
			// .tendency(vo.getTendency())

			// 옵션 리스트
			.optionList(savingDtoList)
			.build();
	}

	/**
	 * 주담대 VO를 DTO로 변환
	 * @param vo
	 * @return
	 */
	public static MortgageDto toMortgageDto(MortgageVo vo) {
		List<MortgageOptionDto> mortgageDtoList = vo.getOptionList().stream()
			.map(option -> MortgageOptionDto.builder()
				.mrtgTypeNm(option.getMrtgTypeNm())
				.rpayTypeNm(option.getRpayTypeNm())
				.lendRateTypeNm(option.getLendRateTypeNm())
				.lendRateMin(option.getLendRateMin())
				.lendRateMax(option.getLendRateMax())
				.lendRateAvg(option.getLendRateAvg())
				.build())
			.collect(Collectors.toList());

		return MortgageDto.builder()
			// 공통 정보 -부모
			.finPrdtCd(vo.getFinPrdtCd())
			.finPrdtNm(vo.getFinPrdtNm())
			.prdtFeature(vo.getPrdtFeature())

			// 공통 정보 -중간부모
			.korCoNm(vo.getKorCoNm())
			.finPrdtCategory(vo.getFinPrdtCategory())
			.description(vo.getDescription())
			.joinWay(vo.getJoinWay())
			.recReason(vo.getRecReason())

			// 주택담보대출 전용 정보
			.loanInciExpn(vo.getLoanInciExpn())
			.erlyRpayFee(vo.getErlyRpayFee())
			.dlyRate(vo.getDlyRate())
			.loanLmt(vo.getLoanLmt())

			// 옵션 리스트
			.optionList(mortgageDtoList)
			.build();
	}

	/**
	 * 펀드 VO를 DTO로 변환
	 * @param vo
	 * @return
	 */
	public static FundDto toFundDto(FundVo vo) {
		List<FundOptionDto> fundDtoList = vo.getOptionList().stream()
			.map(option -> FundOptionDto.builder()
				.rate3mon(option.getRate3mon())
				.startDate(option.getStartDate())
				.assetTotal(option.getAssetTotal())
				.totalFee(option.getTotalFee())
				.riskGrade(option.getRiskGrade())
				.feeFirst(option.getFeeFirst())
				.feeRedemp(option.getFeeRedemp())
				.priceStd(option.getPriceStd())
				.build())
			.collect(Collectors.toList());
		return FundDto.builder()
			// 공통 정보 -부모
			.finPrdtCd(vo.getFinPrdtCd())
			.finPrdtNm(vo.getFinPrdtNm())
			.prdtFeature(vo.getPrdtFeature())
			// 공통 정보 -중간부모
			.korCoNm(vo.getKorCoNm())
			.finPrdtCategory(vo.getFinPrdtCategory())
			.description(vo.getDescription())
			.joinWay(vo.getJoinWay())
			.recReason(vo.getRecReason())

			// 펀드 전용 정보
			// .tendency(vo.getTendency())

			// 옵션 리스트
			.optionList(fundDtoList)
			.build();
	}

	/**
	 * 금 상품 VO를 DTO로 변환
	 * @param vo
	 * @return
	 */
	public static GoldDto toGoldDto(GoldVo vo) {
		return GoldDto.builder()
			// 공통 정보 -부모
			.finPrdtCd(vo.getFinPrdtCd())
			.finPrdtNm(vo.getFinPrdtNm())
			.prdtFeature(vo.getPrdtFeature())

			// 공통 정보 -중간부모
			.korCoNm(vo.getKorCoNm())
			.finPrdtCategory(vo.getFinPrdtCategory())
			.description(vo.getDescription())
			.joinWay(vo.getJoinWay())
			.recReason(vo.getRecReason())

			// 금 상품 전용 정보
			.lot(vo.getLot())
			.currency(vo.getCurrency())
			.etcNote(vo.getEtcNote())

			.build();
	}

	public static TrustDto toTrustDto(TrustVo vo) {
		return TrustDto.builder()
			// 공통 정보 -부모
			.finPrdtCd(vo.getFinPrdtCd())
			.finPrdtNm(vo.getFinPrdtNm())
			.prdtFeature(vo.getPrdtFeature())

			// 공통 정보 -중간부모
			.korCoNm(vo.getKorCoNm())
			.finPrdtCategory(vo.getFinPrdtCategory())
			.description(vo.getDescription())
			.joinWay(vo.getJoinWay())
			.recReason(vo.getRecReason())

			// 신탁 전용 정보
			.basePrice(vo.getBasePrice())
			.yieldRate(vo.getYieldRate())
			.fundType(vo.getFundType())
			.fundStructure(vo.getFundStructure())
			.taxBenefit(vo.getTaxBenefit())
			.saleStartDate(vo.getSaleStartDate())
			.trustFee(vo.getTrustFee())
			.earlyTerminationFee(vo.getEarlyTerminationFee())
			.depositProtection(vo.getDepositProtection())

			.build();
	}
}
