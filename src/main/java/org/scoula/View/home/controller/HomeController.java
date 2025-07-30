package org.scoula.View.home.controller;

import org.scoula.View.home.dto.HomeResponseDto;
import org.scoula.View.home.service.HomeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {

	private final HomeService homeService;

	@GetMapping()
	public ResponseEntity<HomeResponseDto> getHome(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
			return ResponseEntity.ok(homeService.getHomeData(null)); // 비로그인 대응
		}

		String email = auth.getName();
		return ResponseEntity.ok(homeService.getHomeData(email));
	}
}
