package org.scoula.member.service;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.scoula.member.dto.ChangePasswordDTO;
import org.scoula.member.dto.MemberDTO;
import org.scoula.member.dto.MemberJoinDTO;
import org.scoula.member.dto.MemberUpdateDTO;
import org.scoula.member.exception.PasswordMissmatchException;
import org.scoula.member.mapper.MemberMapper;
import org.scoula.security.account.domain.AuthVO;
import org.scoula.security.account.domain.MemberVO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
	final PasswordEncoder passwordEncoder;
	final MemberMapper mapper;

	@Override
	public boolean checkDupplicate(String username) {
		MemberVO member = mapper.findByUsername(username);
		return member != null ? true : false;
	}

	@Override
	public MemberDTO get(String username) {
		MemberVO memberVO = Optional.ofNullable(mapper.get(username))
			.orElseThrow(NoSuchElementException::new);
		return MemberDTO.of(memberVO);
	}
	private void saveAvatar(MultipartFile avatar, String username){
		//아바타 업로드
		if(avatar != null && !avatar.isEmpty()){
			File dest = new File("/Users/dong2/upload/avatar",username+".png");
			try {
				avatar.transferTo(dest);
			}catch (IOException e){
				throw new RuntimeException(e);
			}
		}
	}

	@Transactional
	@Override
	public MemberDTO join(MemberJoinDTO dto) {
		MemberVO member = dto.tOVO();

		member.setPassword(passwordEncoder.encode(member.getPassword())); //비밀번호 암호화
		mapper.insert(member);

		AuthVO auth = new AuthVO();
		auth.setUsername(member.getUsername());
		auth.setAuth("ROLE_MEMBER");
		mapper.insertAuth(auth);

		saveAvatar(dto.getAvatar(),member.getUsername());

		return get(member.getUsername());
	}
	@Override
	public MemberDTO update(MemberUpdateDTO member){
		MemberVO vo = mapper.get(member.getUsername());
		//비밀번호 일치 확인
		if(!passwordEncoder.matches(member.getPassword(),vo.getPassword())){
			throw new PasswordMissmatchException();
		}
		mapper.update(member.toVO());
		saveAvatar(member.getAvatar(),member.getUsername());
		return get(member.getUsername());
	}

	@Override
	public void changePassword(ChangePasswordDTO changePassword) {
		MemberVO member = mapper.get(changePassword.getUsername());

		if(!passwordEncoder.matches(changePassword.getOldPassword(),member.getPassword())){
			throw new PasswordMissmatchException();
		}
		changePassword.setNewPassword(passwordEncoder.encode(changePassword.getNewPassword()));

		mapper.updatePassword(changePassword);
	}
}
