package org.scoula.user.service;

import org.scoula.user.dto.UserDto;

public interface UserService {
	UserDto getUser(String email);

	void join(UserDto userDto);

	void updateConnectedId(String email, String connectedId);

	void updateBranchName(String email, String branchName);
}
