package org.scoula.user.controller;

import java.util.Map;

import org.scoula.user.dto.UserDto;
import org.scoula.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;

	@PostMapping("/join")
	public ResponseEntity<Void> join(@RequestBody UserDto userDto) {
		userService.join(userDto);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{email}")
	public ResponseEntity<UserDto> getUser(@PathVariable String email) {
		UserDto user = userService.getUser(email);
		return ResponseEntity.ok(user);
	}

	@GetMapping("/name")
	public ResponseEntity<String> getUserName(Authentication authentication) {
		String email = authentication.getName();
		return ResponseEntity.ok(userService.getUserName(email));
	}

	@GetMapping("/phone")
	public ResponseEntity<String> getUserPhone(Authentication authentication) {
		String email = authentication.getName();
		return ResponseEntity.ok(userService.getUserPhone(email));
	}

	@GetMapping("/birth")
	public ResponseEntity<String> getUserBirth(Authentication authentication) {
		String email = authentication.getName();
		return ResponseEntity.ok(userService.getBirth(email));
	}

	@GetMapping("/branch")
	public ResponseEntity<String> getBranchName(Authentication authentication) {
		String email = authentication.getName();
		return ResponseEntity.ok(userService.getBranchName(email));
	}

	@GetMapping("/connected-id")
	public ResponseEntity<String> getConnectedId(Authentication authentication) {
		String email = authentication.getName();
		return ResponseEntity.ok(userService.getConnectedId(email));
	}

	@PatchMapping("/branch")
	public ResponseEntity<String> updateBranchName(
		Authentication authentication,
		@RequestBody Map<String, String> requestBody) {

		String email = authentication.getName();  // 인증된 사용자 이메일
		String newBranchName = requestBody.get("branchName"); // 요청 바디에서 branchName 추출

		if (newBranchName == null || newBranchName.isEmpty()) {
			return ResponseEntity.badRequest().body("branchName must be provided");
		}

		userService.updateBranchName(email, newBranchName);

		return ResponseEntity.ok("Branch name updated successfully");
	}
}
