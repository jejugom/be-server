package org.scoula.user.domain;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
public class AuthVO implements GrantedAuthority {
	private String email;
	private String auth;

	@Override
	public String getAuthority(){
		return auth;
	}
}
