package org.scoula.asset.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.scoula.asset.dto.AssetDetailResponseDto;
import org.scoula.asset.mapper.AssetDetailMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssetDetailServiceImpl implements AssetDetailService {

	private final AssetDetailMapper assetDetailMapper;

	@Override
	public List<AssetDetailResponseDto> getAssetDetailsByEmail(String email) {
		return assetDetailMapper.getAssetDetailsByEmail(email).stream()
			.map(AssetDetailResponseDto::of)
			.collect(Collectors.toList());
	}

	@Override
	public AssetDetailResponseDto getAssetDetailById(Integer assetId) {
		return Optional.ofNullable(assetDetailMapper.getAssetDetailById(assetId))
			.map(AssetDetailResponseDto::of)
			.orElseThrow(() -> new NoSuchElementException("AssetDetail not found with id: " + assetId));
	}

	@Override
	public void addAssetDetail(AssetDetailResponseDto assetDetailResponseDto) {
		assetDetailMapper.insertAssetDetail(assetDetailResponseDto.toVo());
	}

	@Override
	public void updateAssetDetail(AssetDetailResponseDto assetDetailResponseDto) {
		if (assetDetailMapper.updateAssetDetail(assetDetailResponseDto.toVo()) == 0) {
			throw new NoSuchElementException("AssetDetail not found with id: " + assetDetailResponseDto.getAssetId());
		}
	}

	@Override
	public void deleteAssetDetail(Integer assetId, String userEmail) {
		if (assetDetailMapper.deleteAssetDetail(assetId, userEmail) == 0) {
			throw new NoSuchElementException("AssetDetail not found with id: " + assetId + " for user " + userEmail);
		}
	}
}
