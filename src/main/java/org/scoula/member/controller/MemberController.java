package org.scoula.member.controller;

import java.io.File;

import javax.servlet.http.HttpServletResponse;

import org.scoula.common.util.UploadFiles;
import org.scoula.member.dto.ChangePasswordDTO;
import org.scoula.member.dto.MemberDTO;
import org.scoula.member.dto.MemberJoinDTO;
import org.scoula.member.dto.MemberUpdateDTO;
import org.scoula.member.service.MemberService;
import org.scoula.member.service.MemberServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
	final MemberService service;

	@GetMapping("/checkusername/{username}")
	public ResponseEntity<Boolean> checkUsername(@PathVariable String username) {
		return ResponseEntity.ok().body(service.checkDupplicate(username));
	}

	@PostMapping("")
	public ResponseEntity<MemberDTO> join(MemberJoinDTO member) {
		return ResponseEntity.ok(service.join(member));
	}

	@GetMapping("/{username}/avatar")
	public void getAvatar(@PathVariable String username, HttpServletResponse response) {
		String avatarPath = "/Users/dong2/upload/avatar/" + username + ".png";
		File file = new File(avatarPath);
		if (!file.exists()) {//아바타 등록이 없는 경우, 디폴트 아바타 이미지 사용
			file = new File("/Users/dong2/upload/unknown.png");
		}
		UploadFiles.downloadImage(response, file);
	}

	@PostMapping("/{username}")
	public ResponseEntity<MemberDTO> changeProfile(MemberUpdateDTO member) {
		return ResponseEntity.ok(service.update(member));
	}
	@PutMapping("/{username}/changepassword")
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO){
		service.changePassword(changePasswordDTO);
		return ResponseEntity.ok().build()	;
	}
	/**
	 *2. 나머지 메서드들은 @RequestBody를 사용하지 않는 이유
	 * join 과 changeProfile 은 MultipartFile 포함 Form Data 전송이기 때문.
	 * MemberJoinDTO, MemberUpdateDTO는 아바타 업로드(파일 전송)를 포함하기 위해 **Content-Type: multipart/form-data**로 전송됨.
	 * multipart/form-data 방식에서는 @RequestBody 사용이 불가능하며,
	 * Spring MVC가 폼 필드와 파일 필드를 DTO의 필드에 자동 바인딩 해 주기 때문에 DTO 파라미터만으로 받음.
	 * 🌿 3. 왜 changePassword는 @RequestBody를 쓰는가?
	 * ChangePasswordDTO는 단순히: username,oldPassword,newPassword
	 * 만을 받으며, 파일 업로드가 필요 없고 JSON 형태로 받는 것이 RESTful 구조상 자연스러움.
	 * 따라서 프론트는: Content-Type: application/json
	 * JSON 바디로 요청,서버는 @RequestBody ChangePasswordDTO 로 JSON을 객체로 받아 처리.
	 */
}
