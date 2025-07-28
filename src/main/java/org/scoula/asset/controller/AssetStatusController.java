package org.scoula.asset.controller;

import java.util.List;

import org.scoula.asset.dto.AssetStatusRequestDto;
import org.scoula.asset.dto.AssetStatusResponseDto;
import org.scoula.asset.service.AssetStatusService;
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
public class AssetStatusController {

	private final AssetStatusService assetStatusService;

	@GetMapping()
	public ResponseEntity<List<AssetStatusResponseDto>> getAssetStatusByEmail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		List<AssetStatusResponseDto> assetStatusResponseDtos = assetStatusService.getAssetStatusByEmail(userEmail);
		return ResponseEntity.ok(assetStatusResponseDtos);
	}

	@GetMapping("/{assetId}")
	public ResponseEntity<AssetStatusResponseDto> getAssetStatusById(@PathVariable Integer assetId) {
		// 권한 검사는 서비스 계층에서 수행하는 것이 좋습니다.
		return ResponseEntity.ok(assetStatusService.getAssetStatusById(assetId));
	}

	@PostMapping
	public ResponseEntity<Void> addAssetStatus(@RequestBody AssetStatusRequestDto requestDto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		assetStatusService.addAssetStatus(userEmail,requestDto);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{assetId}")
	public ResponseEntity<Void> updateAssetStatus(@PathVariable Integer assetId,
		@RequestBody AssetStatusRequestDto requestDto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		assetStatusService.updateAssetStatus(assetId, userEmail, requestDto);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{assetId}")
	public ResponseEntity<Void> deleteAssetStatus(@PathVariable Integer assetId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		assetStatusService.deleteAssetStatus(assetId, userEmail);
		return ResponseEntity.ok().build();
	}
}
