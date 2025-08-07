package org.scoula.user.dto;

import java.util.Date;

import lombok.Data;

@Data
public class UserInfoUpdateRequestDto {
	private String userName;
	private String userPhone;
	private Date birth;
}