package org.scoula.asset.service;

import lombok.RequiredArgsConstructor;
import org.scoula.asset.dto.AssetInfoDTO;
import org.scoula.asset.mapper.AssetInfoMapper;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssetInfoServiceImpl implements AssetInfoService {

    private final AssetInfoMapper assetInfoMapper;

    @Override
    public AssetInfoDTO getAssetInfoByEmail(String email) {
        return Optional.ofNullable(assetInfoMapper.getAssetInfoByEmail(email))
                .map(AssetInfoDTO::of)
                .orElseThrow(() -> new NoSuchElementException("AssetInfo not found for email: " + email));
    }

    @Override
    public void addAssetInfo(AssetInfoDTO assetInfoDTO) {
        assetInfoMapper.insertAssetInfo(assetInfoDTO.toVO());
    }

    @Override
    public void updateAssetInfo(AssetInfoDTO assetInfoDTO) {
        // if (assetInfoMapper.updateAssetInfo(assetInfoDTO.toVO()) == 0) {
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
