package org.scoula.View.preference.controller;

import org.scoula.View.preference.service.PreferenceService;
import org.scoula.View.preference.dto.PreferenceRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/preference")
public class PreferenceController {
	private final PreferenceService preferenceService;

	@PostMapping()
	public ResponseEntity<Void> setUserPreference(@RequestBody PreferenceRequestDto requestDto){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		preferenceService.setUserPreference(requestDto,userName);
		return ResponseEntity.ok().build();
	}
}
