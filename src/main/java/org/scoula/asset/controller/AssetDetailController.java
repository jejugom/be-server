package org.scoula.asset.controller;

import java.util.List;

import org.scoula.asset.dto.AssetDetailDto;
import org.scoula.asset.service.AssetDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@RequestMapping("/api/assets")
public class AssetDetailController {

	private final AssetDetailService assetDetailService;

	@GetMapping()
	public ResponseEntity<List<AssetDetailDto>> getAssetDetailsByEmail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		return ResponseEntity.ok(assetDetailService.getAssetDetailsByEmail(userEmail));
	}

	@GetMapping("/{assetId}")
	public ResponseEntity<AssetDetailDto> getAssetDetailById(@PathVariable Integer assetId) {
		return ResponseEntity.ok(assetDetailService.getAssetDetailById(assetId));
	}

	@PostMapping
	public ResponseEntity<Void> addAssetDetail(@RequestBody AssetDetailDto assetDetailDto) {
		assetDetailService.addAssetDetail(assetDetailDto);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{assetId}")
	public ResponseEntity<Void> updateAssetDetail(@PathVariable Integer assetId,
		@RequestBody AssetDetailDto assetDetailDto) {
		assetDetailDto.setAssetId(assetId);
		assetDetailService.updateAssetDetail(assetDetailDto);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{assetId}")
	public ResponseEntity<Void> deleteAssetDetail(@PathVariable Integer assetId) {
		assetDetailService.deleteAssetDetail(assetId);
		return ResponseEntity.ok().build();
	}
}
