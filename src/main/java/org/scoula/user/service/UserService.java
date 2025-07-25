package org.scoula.user.service;

import org.scoula.user.dto.UserDto;

public interface UserService {
	UserDto getUser(String email);

	void join(UserDto userDto);

	void updateConnectedId(String email, String connectedId);

	void updateBranchName(String email, String branchName);

	String getUserName(String email);
	String getUserPhone(String email);
	String getBirth(String email); // Date → String으로 변환
	String getBranchName(String email);
	String getConnectedId(String email);


}
