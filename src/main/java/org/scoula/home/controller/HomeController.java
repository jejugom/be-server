package org.scoula.home.controller;

import org.scoula.asset.dto.AssetInfoDto;
import org.scoula.asset.service.AssetInfoService;
import org.scoula.home.dto.HomeResponseDto;
import org.scoula.home.service.HomeService;
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

	private final AssetInfoService assetInfoService;
	private final HomeService homeServie;

	@GetMapping("/user")
	public ResponseEntity<HomeResponseDto> getAssetInfoByEmail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		return ResponseEntity.ok(homeServie.getHomeData(userEmail));
	}
	@GetMapping()
	public ResponseEntity<HomeResponseDto> getHomeToAnonymouse(){
		return ResponseEntity.ok(homeServie.getHomeData(null));
	}
}
