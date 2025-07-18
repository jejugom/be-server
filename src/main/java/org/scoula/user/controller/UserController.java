package org.scoula.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.scoula.user.dto.LoginDTO;
import org.scoula.user.dto.UserDTO;
import org.scoula.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;

	@PostMapping("/join")
	public ResponseEntity<Void> join(@RequestBody UserDTO userDTO) {
		userService.join(userDTO);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/login")
	public ResponseEntity<UserDTO> login(@RequestBody LoginDTO loginDTO) {
		UserDTO user = userService.login(loginDTO);
		return ResponseEntity.ok(user);
	}

	@GetMapping("/{email}")
	public ResponseEntity<UserDTO> getUser(@PathVariable String email) {
		UserDTO user = userService.getUser(email);
		return ResponseEntity.ok(user);
	}
}
