package org.scoula.user.controller;

import org.scoula.user.dto.BranchIdUpdateRequestDto;
import org.scoula.user.dto.MyPageResponseDto;
import org.scoula.user.dto.UserDto;
import org.scoula.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Api(tags = "사용자 API", description = "사용자 관련 API")
public class UserController {

	private final UserService userService;

	@ApiOperation(value = "회원 가입", notes = "사용자가 회원 가입을 진행합니다.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "회원 가입 성공"),
		@ApiResponse(code = 400, message = "잘못된 요청")
	})
	@PostMapping("/join")
	public ResponseEntity<Void> join(@RequestBody UserDto userDto) {
		userService.join(userDto);
		return ResponseEntity.ok().build();
	}

	@ApiOperation(value = "회원 정보 조회", notes = "이메일을 기반으로 사용자 정보를 조회합니다.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "회원 정보 조회 성공"),
		@ApiResponse(code = 404, message = "해당 이메일의 회원 없음")
	})
	@GetMapping("/{email}")
	public ResponseEntity<UserDto> getUser(@PathVariable String email) {
		UserDto user = userService.getUser(email);
		return ResponseEntity.ok(user);
	}

	@ApiOperation(value = "지점 ID 수정", notes = "로그인된 사용자의 지점 ID(branchId)를 수정합니다.")
	@ApiResponses({
		@ApiResponse(code = 204, message = "지점 ID 수정 성공"),
		@ApiResponse(code = 400, message = "잘못된 요청"),
		@ApiResponse(code = 403, message = "권한 없음")
	})
	@PatchMapping("/branch")
	public ResponseEntity<Void> updateBranchName(
		Authentication authentication,
		@RequestBody BranchIdUpdateRequestDto requestDto) {

		String email = authentication.getName();
		userService.updateBranchId(email, requestDto.getBranchId());
		return ResponseEntity.noContent().build();
	}

	@ApiOperation(value = "회원 탈퇴", notes = "현재 로그인한 사용자의 계정을 탈퇴 처리합니다.")
	@ApiResponses({
		@ApiResponse(code = 204, message = "회원 탈퇴 성공"),
		@ApiResponse(code = 403, message = "권한 없음")
	})
	@DeleteMapping()
	public ResponseEntity<Void> withdrawUser(Authentication authentication) {
		String email = authentication.getName();
		userService.withdrawUser(email);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/mypage")
	public ResponseEntity<MyPageResponseDto> getMyPageInfo(Authentication authentication) {
		// userDetails.getUsername()을 통해 현재 인증된 사용자의 ID(일반적으로 이메일 또는 회원번호)를 가져옵니다.
		String email = authentication.getName();

		// Service 계층에 사용자 ID를 전달하여 마이페이지에 필요한 데이터를 조회하고 DTO로 조합합니다.
		MyPageResponseDto myPageData = userService.getMyPageData(email);

		// 조회된 데이터를 ResponseEntity.ok()에 담아 200 OK 상태와 함께 반환합니다.
		return ResponseEntity.ok(myPageData);
	}
}
