package org.scoula.asset.service;

import java.util.List;

import org.scoula.asset.domain.AssetStatusVo;
import org.scoula.asset.dto.AssetStatusIdDto;
import org.scoula.asset.dto.AssetStatusRequestDto;
import org.scoula.asset.dto.AssetStatusResponseDto;
import org.scoula.asset.dto.AssetStatusSummaryDto;

public interface AssetStatusService {
	List<AssetStatusResponseDto> getAssetStatusByEmail(String email);

	List<AssetStatusSummaryDto> getAssetStatusSummaryByEmail(String email);

	AssetStatusIdDto addAssetStatus(String email, AssetStatusRequestDto requestDto);

	void updateAssetStatus(Integer assetId, String email, AssetStatusRequestDto requestDto);

	void deleteAssetStatus(Integer assetId, String email);

	/**
	 * 특정 사용자의 모든 자산 목록을 상세 정보(Vo) 그대로 조회합니다.
	 * @param email 사용자 이메일
	 * @return AssetStatusVo 객체 리스트
	 */
	List<AssetStatusVo> getFullAssetStatusByEmail(String email);
}
