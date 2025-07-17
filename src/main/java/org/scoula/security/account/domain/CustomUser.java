package org.scoula.security.account.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomUser extends User {
	private MemberVO member;
	public CustomUser(String username, String password,
		Collection<? extends  GrantedAuthority> authorities){
		super(username,password,authorities);
	}
	public CustomUser(MemberVO vo){
		super(vo.getUsername(),vo.getPassword(), vo.getAuthList());
		this.member = vo;
	}
}
