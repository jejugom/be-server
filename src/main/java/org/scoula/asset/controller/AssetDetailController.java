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

    @GetMapping("/{id}")
    public ResponseEntity<AssetDetailDTO> getAssetDetailById(@PathVariable Integer id) {
        return ResponseEntity.ok(assetDetailService.getAssetDetailById(id));
    }

    @PostMapping
    public ResponseEntity<Void> addAssetDetail(@RequestBody AssetDetailDTO assetDetailDTO) {
        assetDetailService.addAssetDetail(assetDetailDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAssetDetail(@PathVariable Integer id, @RequestBody AssetDetailDTO assetDetailDTO) {
        assetDetailDTO.setId(id);
        assetDetailService.updateAssetDetail(assetDetailDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssetDetail(@PathVariable Integer id) {
        assetDetailService.deleteAssetDetail(id);
        return ResponseEntity.ok().build();
    }
}
