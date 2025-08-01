package org.scoula.asset.service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.scoula.asset.domain.AssetStatusVo;
import org.scoula.asset.dto.AssetStatusIdDto;
import org.scoula.asset.dto.AssetStatusRequestDto;
import org.scoula.asset.dto.AssetStatusResponseDto;
import org.scoula.asset.dto.AssetStatusSummaryDto;
import org.scoula.asset.mapper.AssetStatusMapper;
import org.scoula.recommend.service.CustomRecommendService;
import org.scoula.user.dto.UserDto;
import org.scoula.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional //2개 이상의 DB조회가 이루어지기 때문에 항상 Transactional 처리가 되도록.
public class AssetStatusServiceImpl implements AssetStatusService {
	/**
	 * 이 AssetStatusService 클래스에서는,,,
	 * User가 자산 정보를 업데이트, 삭제, 추가할 때,
	 * 유저 자산 정보 (asset_status) 를 업데이트 할 뿐만 아니라
	 * 자산의 비중을 토대로 사용자 테이블의 asset_proportion의 값을 -1~1 사이로 산정합니다.
	 */
	private final AssetStatusMapper assetStatusMapper;
	private final UserService userService;
	private final CustomRecommendService customRecommendService;

	private static final Map<String, Double> assetWeights = Map.of(
		"1", 0.4,// 부동산
		"2", 1.0, //예적금
		"3", 0.7, //현금
		"4", -1.0, //주신 및 펀드
		"5", -0.8,//사업체 및 지분
		"6", 0.0 //기타
	);

	private double getUserAssetProportionRate(String userEmail) {
		List<AssetStatusVo> assets = assetStatusMapper.findAssetStatusByEmail(userEmail);
		double totalAmount = assets.stream().mapToDouble(AssetStatusVo::getAmount).sum();
		if (totalAmount == 0)
			return 0.0;

		double weightedSum = assets.stream()
			.mapToDouble(vo -> vo.getAmount() * assetWeights.getOrDefault(vo.getAssetCategoryCode(), 0.0))
			.sum();

		return weightedSum / totalAmount;
	}

	private double calculateTotalAsset(String email) {
		List<AssetStatusVo> assets = assetStatusMapper.findAssetStatusByEmail(email);

		for (AssetStatusVo vo : assets) {
			if (vo.getAmount() == null) {
				throw new IllegalArgumentException(
					"자산 금액(amount)이 null입니다.해당 자산 삭제/변경 필요. \n assetId: " + vo.getAssetId());
			}
		}

		return assets.stream()
			.mapToDouble(vo -> vo.getAmount().doubleValue())
			.sum();
	}

	private void updateUserAssetSummary(String userEmail) {
		UserDto userDto = userService.getUser(userEmail);
		userDto.setAsset((long)calculateTotalAsset(userEmail));
		userDto.setAssetProportion(getUserAssetProportionRate(userEmail));
		userService.updateUser(userEmail, userDto);
		//자산 업데이트 시 추천 상품도 수정
		customRecommendService.addCustomRecommend(userEmail);
	}

	@Override
	public List<AssetStatusResponseDto> getAssetStatusByEmail(String email) {
		return assetStatusMapper.findAssetStatusByEmail(email).stream()
			.map(AssetStatusResponseDto::of)
			.collect(Collectors.toList());
	}

	@Override
	public List<AssetStatusSummaryDto> getAssetStatusSummaryByEmail(String email) {
		return assetStatusMapper.findAssetStatusSummaryByEmail(email).stream()
			.map(AssetStatusSummaryDto::of)
			.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public AssetStatusIdDto addAssetStatus(String email, AssetStatusRequestDto requestDto) {
		// 1. assetStatusVo 객체 생성 (이때 assetId는 null 또는 0)
		AssetStatusVo assetStatusVo = requestDto.toVo();
		assetStatusVo.setEmail(email);

		// 2. mapper 메서드 호출. 이 메서드는 void지만,
		//    내부적으로 assetStatusVo 객체의 assetId를 채워줍니다.
		assetStatusMapper.insertAssetStatus(assetStatusVo);

		// 3. 이 시점에는 assetStatusVo.getAssetId()를 호출하면
		//    DB에 생성된 ID 값이 정상적으로 나옵니다.
		updateUserAssetSummary(email);

		return new AssetStatusIdDto(assetStatusVo.getAssetId());
	}

	@Override
	public void updateAssetStatus(Integer assetId, String email, AssetStatusRequestDto requestDto) {
		AssetStatusVo assetStatusVo = AssetStatusVo.builder()
			.assetId(assetId)
			.email(email)
			.assetCategoryCode(requestDto.getAssetCategoryCode())
			.amount(requestDto.getAmount())
			.assetName(requestDto.getAssetName())
			.businessType(requestDto.getBusinessType())
			.build();

		if (assetStatusMapper.updateAssetStatus(assetStatusVo) == 0) {
			throw new NoSuchElementException("목록 번호 오기입 / 권한이 없습니다.");
		}

		// updateUserAssetSummary를 호출해 사용자 테이블에 총 자산에 추가된 자산 금액 저장
		updateUserAssetSummary(email);
	}

	@Override
	public void deleteAssetStatus(Integer assetId, String email) {
		System.out.println("=== DELETE ASSET DEBUG ===");
		System.out.println("assetId: " + assetId);
		System.out.println("email: " + email);
		
		int deleteResult = assetStatusMapper.deleteAssetStatus(assetId, email);
		System.out.println("deleteResult: " + deleteResult);
		
		if (deleteResult == 0) {
			throw new NoSuchElementException("해당 자산이 사용자 계정에 존재하지 않습니다. ");
		}

		// updateUserAssetSummary를 호출해 사용자 테이블에 총 자산에 추가된 자산 금액 저장
		updateUserAssetSummary(email);
	}
}
