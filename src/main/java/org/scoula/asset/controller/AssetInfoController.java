package org.scoula.asset.controller;

import lombok.RequiredArgsConstructor;
import org.scoula.asset.dto.AssetInfoDTO;
import org.scoula.asset.service.AssetInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/asset-info")
public class AssetInfoController {

    private final AssetInfoService assetInfoService;

    @GetMapping("/{email}")
    public ResponseEntity<AssetInfoDTO> getAssetInfoByEmail(@PathVariable String email) {
        return ResponseEntity.ok(assetInfoService.getAssetInfoByEmail(email));
    }

    @PostMapping
    public ResponseEntity<Void> addAssetInfo(@RequestBody AssetInfoDTO assetInfoDTO) {
        assetInfoService.addAssetInfo(assetInfoDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{email}")
    public ResponseEntity<Void> updateAssetInfo(@PathVariable String email, @RequestBody AssetInfoDTO assetInfoDTO) {
        assetInfoDTO.setEmail(email);
        assetInfoService.updateAssetInfo(assetInfoDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteAssetInfo(@PathVariable String email) {
        assetInfoService.deleteAssetInfo(email);
        return ResponseEntity.ok().build();
    }
}
