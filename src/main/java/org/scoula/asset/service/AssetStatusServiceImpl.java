package org.scoula.asset.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.scoula.asset.domain.AssetStatusVo;
import org.scoula.asset.dto.AssetStatusRequestDto;
import org.scoula.asset.dto.AssetStatusResponseDto;
import org.scoula.asset.mapper.AssetStatusMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssetStatusServiceImpl implements AssetStatusService {

	private final AssetStatusMapper assetStatusMapper;

	@Override
	public List<AssetStatusResponseDto> getAssetStatusByEmail(String email) {
		return assetStatusMapper.findAssetStatusByEmail(email).stream()
			.map(AssetStatusResponseDto::of)
			.collect(Collectors.toList());
	}

	@Override
	public AssetStatusResponseDto getAssetStatusById(Integer assetId) {
		return Optional.ofNullable(assetStatusMapper.findAssetStatusById(assetId))
			.map(AssetStatusResponseDto::of)
			.orElseThrow(() -> new NoSuchElementException("AssetStatus not found with id: " + assetId));
	}

	@Override
	public void addAssetStatus(String email, AssetStatusRequestDto requestDto) {
		AssetStatusVo assetStatusVo = requestDto.toVo();
		assetStatusVo.setEmail(email);
		assetStatusMapper.insertAssetStatus(assetStatusVo);
	}

	@Override
	public void updateAssetStatus(Integer assetId, String email, AssetStatusRequestDto requestDto) {
		AssetStatusVo assetStatusVo = requestDto.toVo();
		assetStatusVo.setAssetId(assetId);
		assetStatusVo.setEmail(email);
		if (assetStatusMapper.updateAssetStatus(assetStatusVo) == 0) {
			throw new NoSuchElementException("AssetStatus not found with id: " + assetId);
		}
	}

	@Override
	public void deleteAssetStatus(Integer assetId, String userEmail) {
		if (assetStatusMapper.deleteAssetStatus(assetId, userEmail) == 0) {
			throw new NoSuchElementException("AssetStatus not found with id: " + assetId + " for user " + userEmail);
		}
	}
}
