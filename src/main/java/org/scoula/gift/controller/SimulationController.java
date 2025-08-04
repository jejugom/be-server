package org.scoula.gift.controller;

import org.scoula.gift.dto.SimulationRequestDto;
import org.scoula.gift.dto.SimulationResponseDto;
import org.scoula.gift.service.SimulationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/gift")
@RequiredArgsConstructor
public class SimulationController { // 컨트롤러 이름도 조금 더 명확하게 변경

	private final SimulationService simulationService;

	@PostMapping("/simulation")
	public ResponseEntity<SimulationResponseDto> runSimulation(
		@RequestBody SimulationRequestDto requestDto, Authentication authentication) {

		String email = authentication.getName();

		// 포괄적인 서비스 메서드 호출
		SimulationResponseDto responseDto = simulationService.runGiftTaxSimulation(requestDto, email);

		return ResponseEntity.ok(responseDto);
	}
}