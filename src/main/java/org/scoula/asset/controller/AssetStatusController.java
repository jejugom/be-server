package org.scoula.asset.controller;

import java.util.List;

import org.scoula.asset.dto.AssetStatusIdDto;
import org.scoula.asset.dto.AssetStatusRequestDto;
import org.scoula.asset.dto.AssetStatusResponseDto;
import org.scoula.asset.service.AssetStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
	public ResponseEntity<List<AssetStatusResponseDto>> getAssetStatusByEmail(Authentication authentication) {
		String userEmail = authentication.getName();
		List<AssetStatusResponseDto> assetStatusResponseDtos = assetStatusService.getAssetStatusByEmail(userEmail);
		return ResponseEntity.ok(assetStatusResponseDtos);
	}

	@PostMapping
	public ResponseEntity<AssetStatusIdDto> addAssetStatus(@RequestBody AssetStatusRequestDto requestDto, Authentication authentication) {
		String userEmail = authentication.getName();
		AssetStatusIdDto responseDto = assetStatusService.addAssetStatus(userEmail, requestDto);

		// 리소스가 성공적으로 생성되었음을 의미하는 201 Created 상태와 함께
		// 생성된 ID를 응답 본문에 담아 반환합니다.
		return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	}


	@PutMapping("/{assetId}")
	public ResponseEntity<Void> updateAssetStatus(@PathVariable Integer assetId,
		@RequestBody AssetStatusRequestDto requestDto, Authentication authentication) {
		String userEmail = authentication.getName();
		assetStatusService.updateAssetStatus(assetId, userEmail, requestDto);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{assetId}")
	public ResponseEntity<Void> deleteAssetStatus(@PathVariable Integer assetId, Authentication authentication) {
		String email = authentication.getName();
		assetStatusService.deleteAssetStatus(assetId, email);
		return ResponseEntity.noContent().build();
	}
}
