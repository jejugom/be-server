package org.scoula.member.mapper;

import org.scoula.member.dto.ChangePasswordDTO;
import org.scoula.security.account.domain.AuthVO;
import org.scoula.security.account.domain.MemberVO;

public interface MemberMapper {
	MemberVO get(String name);
	MemberVO findByUsername(String username) ; //id 중복체크 시 사용
	int insert(MemberVO memberVO); //회원 정보 추가
	int insertAuth(AuthVO authVO); //회원 권한 정ㅇ보 추가

	int update(MemberVO member);

	int updatePassword(ChangePasswordDTO changePasswordDTO);

}
