package org.scoula.asset.controller;

import org.scoula.asset.dto.AssetInfoDto;
import org.scoula.asset.service.AssetInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/asset-info")
public class AssetInfoController {

	private final AssetInfoService assetInfoService;

	@GetMapping("/{email}")
	public ResponseEntity<AssetInfoDto> getAssetInfoByEmail(@PathVariable String email) {
		return ResponseEntity.ok(assetInfoService.getAssetInfoByEmail(email));
	}

	@PostMapping
	public ResponseEntity<Void> addAssetInfo(@RequestBody AssetInfoDto assetInfoDto) {
		assetInfoService.addAssetInfo(assetInfoDto);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{email}")
	public ResponseEntity<Void> updateAssetInfo(@PathVariable String email, @RequestBody AssetInfoDto assetInfoDto) {
		assetInfoDto.setEmail(email);
		assetInfoService.updateAssetInfo(assetInfoDto);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{email}")
	public ResponseEntity<Void> deleteAssetInfo(@PathVariable String email) {
		assetInfoService.deleteAssetInfo(email);
		return ResponseEntity.ok().build();
	}
}
