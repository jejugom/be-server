package org.scoula.user.controller;

import org.scoula.user.dto.BranchIdUpdateRequestDto;
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

	@PatchMapping("/branch")
	public ResponseEntity<Void> updateBranchName(
		Authentication authentication,
		@RequestBody BranchIdUpdateRequestDto requestDto) {

		String email = authentication.getName();
		userService.updateBranchId(email, requestDto.getBranchId());

		return ResponseEntity.noContent().build();
	}
}
