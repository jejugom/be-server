package org.scoula.member.dto;

import org.scoula.security.account.domain.MemberVO;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateDTO {
	private String username;
	private String password;
	private String email;

	MultipartFile avatar;

	public MemberVO toVO(){
		return MemberVO.builder()
			.username(username)
			.email(email)
			.build();
	}
}
