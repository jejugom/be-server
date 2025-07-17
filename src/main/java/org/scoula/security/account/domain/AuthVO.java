package org.scoula.security.account.domain;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
public class AuthVO implements GrantedAuthority {
	private String username;
	private String auth;

	@Override
	public String getAuthority(){
		return auth;
	}
}
