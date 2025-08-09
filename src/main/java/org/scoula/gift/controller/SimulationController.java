package org.scoula.gift.controller;

import org.scoula.gift.dto.SimulationRequestDto;
import org.scoula.gift.dto.SimulationResponseDto;
import org.scoula.gift.dto.WillPageResponseDto;
import org.scoula.gift.service.SimulationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/gift")
@RequiredArgsConstructor
public class SimulationController {

	private final SimulationService simulationService;

	@PostMapping("/simulation")
	public ResponseEntity<SimulationResponseDto> runSimulation(
		@RequestBody SimulationRequestDto requestDto, Authentication authentication) {

		String email = authentication.getName();

		// 포괄적인 서비스 메서드 호출
		SimulationResponseDto responseDto = simulationService.runGiftTaxSimulation(requestDto, email);

		return ResponseEntity.ok(responseDto);
	}

	/**
	 * 유언장 템플릿 페이지에 필요한 현재 로그인된 사용자 정보를 조회합니다.
	 */
	@GetMapping("/simulation/will")
	public ResponseEntity<WillPageResponseDto> getWillTemplateUserInfo(
		Authentication authentication) {

		// 1. Spring Security Context에서 현재 로그인된 사용자의 이메일(username)을 가져옵니다.
		String userEmail = authentication.getName();

		// 2. Service 레이어에 로직 처리를 위임합니다.
		WillPageResponseDto responseDto = simulationService.getUserInfoForWillPage(userEmail);

		// 3. 성공적인 응답(200 OK)과 함께 DTO를 반환합니다.
		return ResponseEntity.ok(responseDto);
	}

}
