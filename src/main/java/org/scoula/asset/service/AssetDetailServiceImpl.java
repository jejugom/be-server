package org.scoula.asset.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.scoula.asset.dto.AssetDetailDto;
import org.scoula.asset.mapper.AssetDetailMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssetDetailServiceImpl implements AssetDetailService {

	private final AssetDetailMapper assetDetailMapper;

	@Override
	public List<AssetDetailDto> getAssetDetailsByEmail(String email) {
		return assetDetailMapper.getAssetDetailsByEmail(email).stream()
			.map(AssetDetailDto::of)
			.collect(Collectors.toList());
	}

	@Override
	public AssetDetailDto getAssetDetailById(Integer assetId) {
		return Optional.ofNullable(assetDetailMapper.getAssetDetailById(assetId))
			.map(AssetDetailDto::of)
			.orElseThrow(() -> new NoSuchElementException("AssetDetail not found with id: " + assetId));
	}

	@Override
	public void addAssetDetail(AssetDetailDto assetDetailDto) {
		assetDetailMapper.insertAssetDetail(assetDetailDto.toVo());
	}

	@Override
	public void updateAssetDetail(AssetDetailDto assetDetailDto) {
		if (assetDetailMapper.updateAssetDetail(assetDetailDto.toVo()) == 0) {
			throw new NoSuchElementException("AssetDetail not found with id: " + assetDetailDto.getAssetId());
		}
	}

	@Override
	public void deleteAssetDetail(Integer assetId) {
		if (assetDetailMapper.deleteAssetDetail(assetId) == 0) {
			throw new NoSuchElementException("AssetDetail not found with id: " + assetId);
		}
	}

	@Override
	public void saveAssetDetail(AssetDetailDto assetDetailDto) {
		assetDetailMapper.insertAssetDetailWithGeneratedKey(assetDetailDto.toVo());
	}
}
