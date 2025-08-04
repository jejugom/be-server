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
import org.scoula.exception.AssetNotFoundException;
import org.scoula.recommend.service.CustomRecommendService;
import org.scoula.user.dto.UserDto;
import org.scoula.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
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
		// 1. 자산 목록을 DB에서 한 번만 조회합니다.
		List<AssetStatusVo> assets = assetStatusMapper.findAssetStatusByEmail(userEmail);

		double totalAmount = 0;
		double weightedSum = 0;

		for (AssetStatusVo vo : assets) {
			if (vo.getAmount() == null) {
				throw new IllegalArgumentException(
					"자산 금액(amount)이 null입니다. 해당 자산 삭제/변경 필요. \n assetId: " + vo.getAssetId());
			}
			double amount = vo.getAmount().doubleValue();
			totalAmount += amount;
			weightedSum += amount * assetWeights.getOrDefault(vo.getAssetCategoryCode(), 0.0);
		}

		// 2. 계산을 수행합니다.
		double assetProportionRate = (totalAmount == 0) ? 0.0 : weightedSum / totalAmount;

		// 3. 사용자 정보를 업데이트합니다.
		UserDto userDto = userService.getUser(userEmail);
		userDto.setAsset((long)totalAmount);
		userDto.setAssetProportion(assetProportionRate);
		userService.updateUser(userEmail, userDto);

		// 4. 추천 상품을 업데이트합니다.
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
		// toVo()를 사용해 기본 객체를 생성하고, 추가 정보만 설정
		AssetStatusVo assetStatusVo = requestDto.toVo();
		assetStatusVo.setAssetId(assetId);
		assetStatusVo.setEmail(email);

		if (assetStatusMapper.updateAssetStatus(assetStatusVo) == 0) {
			throw new AssetNotFoundException("ID가 " + assetId + "인 자산을 찾을 수 없거나 삭제할 권한이 없습니다.");
		}

		updateUserAssetSummary(email);
	}

	public void deleteAssetStatus(Integer assetId, String email) {
		log.debug("Deleting asset. assetId: {}, email: {}", assetId, email);

		int deleteResult = assetStatusMapper.deleteAssetStatus(assetId, email);
		log.debug("Deletion result count: {}", deleteResult);

		if (deleteResult == 0) {
			throw new NoSuchElementException("해당 자산이 사용자 계정에 존재하지 않습니다.");
		}

		updateUserAssetSummary(email);
	}

	/**
	 * 특정 사용자의 모든 자산 목록을 조회합니다.
	 * DB 조회를 위해 AssetStatusMapper를 호출합니다.
	 * @param email 사용자 이메일
	 * @return AssetStatusVo 객체 리스트
	 */
	@Override
	public List<AssetStatusVo> getFullAssetStatusByEmail(String email) {
		// 매퍼에 이미 존재하는 findAssetStatusByEmail 메소드를 그대로 호출합니다.
		return assetStatusMapper.findAssetStatusByEmail(email);
	}
}
