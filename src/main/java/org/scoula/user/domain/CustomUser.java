package org.scoula.user.domain;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUser extends User {
	//commitTest
	private UserVo user;

	public CustomUser(String username, String password,
		Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	public CustomUser(UserVo userVO) {
		super(userVO.getEmail(), "", new ArrayList<>()); // 권한이 없으므로 빈 리스트 전달
		this.user = userVO;
	}

	public UserVo getUser() {
		return user;
	}
}
