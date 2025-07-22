package org.scoula.asset.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.scoula.asset.dto.AssetInfoDto;
import org.scoula.asset.mapper.AssetInfoMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssetInfoServiceImpl implements AssetInfoService {

	private final AssetInfoMapper assetInfoMapper;

	@Override
	public AssetInfoDto getAssetInfoByEmail(String email) {
		return Optional.ofNullable(assetInfoMapper.getAssetInfoByEmail(email))
			.map(AssetInfoDto::from)
			.orElseThrow(() -> new NoSuchElementException("AssetInfo not found for email: " + email));
	}

	@Override
	public void addAssetInfo(AssetInfoDto assetInfoDto) {
		assetInfoMapper.insertAssetInfo(assetInfoDto.toVO());
	}

	@Override
	public void updateAssetInfo(AssetInfoDto assetInfoDto) {
		// if (assetInfoMapper.updateAssetInfo(assetInfoDto.toVo()) == 0) {
		//     throw new NoSuchElementException("AssetInfo not found for email: " + assetInfoDTO.getEmail());
		// }
	}

	@Override
	public void deleteAssetInfo(String email) {
		// if (assetInfoMapper.deleteAssetInfo(email) == 0) {
		//     throw new NoSuchElementException("AssetInfo not found for email: " + email);
		// }
	}
}
