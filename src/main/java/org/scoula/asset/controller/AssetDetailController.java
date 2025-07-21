package org.scoula.asset.controller;

import lombok.RequiredArgsConstructor;
import org.scoula.asset.dto.AssetDetailDTO;
import org.scoula.asset.service.AssetDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/asset-details")
public class AssetDetailController {

    private final AssetDetailService assetDetailService;

    @GetMapping("/user/{email}")
    public ResponseEntity<List<AssetDetailDTO>> getAssetDetailsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(assetDetailService.getAssetDetailsByEmail(email));
    }

    @GetMapping("/{assetId}")
    public ResponseEntity<AssetDetailDTO> getAssetDetailById(@PathVariable Integer assetId) {
        return ResponseEntity.ok(assetDetailService.getAssetDetailById(assetId));
    }

    @PostMapping
    public ResponseEntity<Void> addAssetDetail(@RequestBody AssetDetailDTO assetDetailDTO) {
        assetDetailService.addAssetDetail(assetDetailDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{assetId}")
    public ResponseEntity<Void> updateAssetDetail(@PathVariable Integer assetId, @RequestBody AssetDetailDTO assetDetailDTO) {
        assetDetailDTO.setAssetId(assetId);
        assetDetailService.updateAssetDetail(assetDetailDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{assetId}")
    public ResponseEntity<Void> deleteAssetDetail(@PathVariable Integer assetId) {
        assetDetailService.deleteAssetDetail(assetId);
        return ResponseEntity.ok().build();
    }
}
