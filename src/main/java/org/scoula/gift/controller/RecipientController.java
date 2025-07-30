package org.scoula.gift.controller;

import org.scoula.gift.dto.RecipientListResponseDto;
import org.scoula.gift.dto.RecipientRequestDto;
import org.scoula.gift.dto.RecipientResponseDto;
import org.scoula.gift.service.RecipientService;
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

// =================================================================
// 1. Controller 계층
// =================================================================
@RestController
@RequestMapping("/api/gift")
public class RecipientController {

	private final RecipientService recipientService;

	// 생성자를 통한 의존성 주입
	public RecipientController(RecipientService recipientService) {
		this.recipientService = recipientService;
	}

	/**
	 * 수증자 정보 생성 (POST)
	 * @param requestDto 수증자 생성을 위한 DTO
	 * @param authentication 현재 인증된 사용자 정보
	 * @return 생성된 수증자 정보
	 */
	@PostMapping
	public ResponseEntity<RecipientResponseDto> createRecipient(
		@RequestBody RecipientRequestDto requestDto,
		Authentication authentication) {

		String email = authentication.getName();
		RecipientResponseDto responseDto = recipientService.createRecipient(requestDto, email);
		return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	}

	/**
	 * 특정 사용자의 전체 수증자 목록 조회 (GET)
	 * @param authentication 현재 인증된 사용자 정보
	 * @return 수증자 목록
	 */
	@GetMapping
	public ResponseEntity<RecipientListResponseDto> getRecipients(Authentication authentication) {
		String email = authentication.getName();
		RecipientListResponseDto listResponseDto = recipientService.findRecipientsByEmail(email);
		return ResponseEntity.ok(listResponseDto);
	}

	/**
	 * 특정 수증자 정보 상세 조회 (GET)
	 * @param recipientId 조회할 수증자 ID
	 * @param authentication 현재 인증된 사용자 정보
	 * @return 특정 수증자 정보
	 */
	@GetMapping("/{recipientId}")
	public ResponseEntity<RecipientResponseDto> getRecipient(
		@PathVariable Integer recipientId,
		Authentication authentication) {

		String email = authentication.getName();
		RecipientResponseDto responseDto = recipientService.findRecipientByIdAndEmail(recipientId, email);
		if (responseDto != null) {
			return ResponseEntity.ok(responseDto);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * 수증자 정보 수정 (PUT)
	 * @param recipientId 수정할 수증자 ID
	 * @param requestDto 수정할 내용을 담은 DTO
	 * @param authentication 현재 인증된 사용자 정보
	 * @return 수정된 수증자 정보
	 */
	@PutMapping("/{recipientId}")
	public ResponseEntity<RecipientResponseDto> updateRecipient(
		@PathVariable Integer recipientId,
		@RequestBody RecipientRequestDto requestDto,
		Authentication authentication) {

		String email = authentication.getName();
		RecipientResponseDto updatedDto = recipientService.updateRecipient(recipientId, requestDto, email);
		if (updatedDto != null) {
			return ResponseEntity.ok(updatedDto);
		} else {
			// 수정할 대상이 없거나 권한이 없는 경우
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	/**
	 * 수증자 정보 삭제 (DELETE)
	 * @param recipientId 삭제할 수증자 ID
	 * @param authentication 현재 인증된 사용자 정보
	 * @return 성공 여부
	 */
	@DeleteMapping("/{recipientId}")
	public ResponseEntity<Void> deleteRecipient(
		@PathVariable Integer recipientId,
		Authentication authentication) {

		String email = authentication.getName();
		boolean success = recipientService.deleteRecipient(recipientId, email);
		if (success) {
			return ResponseEntity.noContent().build(); // 성공 시 204 No Content
		} else {
			return ResponseEntity.notFound().build(); // 삭제할 대상이 없거나 권한이 없는 경우
		}
	}
}