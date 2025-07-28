package org.scoula.asset.controller;

import java.util.List;

import org.scoula.asset.dto.AssetDetailResponseDto;
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
	public ResponseEntity<List<AssetDetailResponseDto>> getAssetDetails() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		List<AssetDetailResponseDto> assetDetailResponseDtos = assetDetailService.getAssetDetailsByEmail(userEmail);
		return ResponseEntity.ok(assetDetailResponseDtos);
	}

	@GetMapping("/{assetId}")
	public ResponseEntity<AssetDetailResponseDto> getAssetDetailById(@PathVariable Integer assetId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();

		// 서비스에서 assetId와 userEmail을 함께 확인하여 권한 검사를 수행하는 로직 작성.
		List<AssetDetailResponseDto> assetDetailResponseDtos = assetDetailService.getAssetDetailsByEmail(userEmail);


		return ResponseEntity.ok(assetDetailService.getAssetDetailById(assetId));
	}

	@PostMapping
	public ResponseEntity<Void> addAssetDetail(@RequestBody AssetDetailResponseDto assetDetailResponseDto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		assetDetailResponseDto.setEmail(userEmail);
		assetDetailService.addAssetDetail(assetDetailResponseDto);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{assetId}")
	public ResponseEntity<Void> updateAssetDetail(@PathVariable Integer assetId,
		@RequestBody AssetDetailResponseDto assetDetailResponseDto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		assetDetailResponseDto.setAssetId(assetId);
		assetDetailResponseDto.setEmail(userEmail);
		assetDetailService.updateAssetDetail(assetDetailResponseDto);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{assetId}")
	public ResponseEntity<Void> deleteAssetDetail(@PathVariable Integer assetId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		assetDetailService.deleteAssetDetail(assetId, userEmail);
		return ResponseEntity.ok().build();
	}
}
