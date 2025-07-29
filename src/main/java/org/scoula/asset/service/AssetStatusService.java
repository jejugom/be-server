package org.scoula.asset.service;

import java.util.List;

import org.scoula.asset.dto.AssetStatusRequestDto;
import org.scoula.asset.dto.AssetStatusResponseDto;

public interface AssetStatusService {
	List<AssetStatusResponseDto> getAssetStatusByEmail(String email);

	void addAssetStatus(String email, AssetStatusRequestDto requestDto);

	void updateAssetStatus(Integer assetId, String email, AssetStatusRequestDto requestDto);

	void deleteAssetStatus(Integer assetId, String userEmail);

}
