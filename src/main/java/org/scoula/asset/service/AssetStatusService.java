package org.scoula.asset.service;

import java.util.List;

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
}
