package org.scoula.asset.service;

import lombok.RequiredArgsConstructor;
import org.scoula.asset.dto.AssetDetailDTO;
import org.scoula.asset.mapper.AssetDetailMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetDetailServiceImpl implements AssetDetailService {

    private final AssetDetailMapper assetDetailMapper;

    @Override
    public List<AssetDetailDTO> getAssetDetailsByEmail(String email) {
        return assetDetailMapper.getAssetDetailsByEmail(email).stream()
                .map(AssetDetailDTO::of)
                .collect(Collectors.toList());
    }

    @Override
    public AssetDetailDTO getAssetDetailById(Integer id) {
        return Optional.ofNullable(assetDetailMapper.getAssetDetailById(id))
                .map(AssetDetailDTO::of)
                .orElseThrow(() -> new NoSuchElementException("AssetDetail not found with id: " + id));
    }

    @Override
    public void addAssetDetail(AssetDetailDTO assetDetailDTO) {
        assetDetailMapper.insertAssetDetail(assetDetailDTO.toVO());
    }

    @Override
    public void updateAssetDetail(AssetDetailDTO assetDetailDTO) {
        // if (assetDetailMapper.updateAssetDetail(assetDetailDTO.toVO()) == 0) {
        //     throw new NoSuchElementException("AssetDetail not found with id: " + assetDetailDTO.getId());
        // }
    }

    @Override
    public void deleteAssetDetail(Integer id) {
        // if (assetDetailMapper.deleteAssetDetail(id) == 0) {
        //     throw new NoSuchElementException("AssetDetail not found with id: " + id);
        // }
    }
}
