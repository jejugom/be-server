package org.scoula.user.dto;

import java.util.Date;

import org.scoula.user.domain.UserVo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
	private String email;
	private String userName;
	private String userPhone;
	private Date birth;
	private String branchName;
	private String connectedId;
	private String branchBranchName;

	public static UserDto of(UserVo user) {
		return UserDto.builder()
			.email(user.getEmail())
			.userName(user.getUserName())
			.userPhone(user.getUserPhone())
			.birth(user.getBirth())
			.branchName(user.getBranchName())
			.connectedId(user.getConnectedId())
			.branchBranchName(user.getBranchBranchName())
			.build();
	}

	public UserVo toVO() {
		return UserVo.builder()
			.email(email)
			.userName(userName)
			.userPhone(userPhone)
			.birth(birth)
			.branchName(branchName)
			.connectedId(connectedId)
			.branchBranchName(branchBranchName)
			.build();
	}
}
