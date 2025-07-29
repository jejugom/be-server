package org.scoula.user.service;

import org.scoula.user.dto.UserDto;

public interface UserService {
	UserDto getUser(String email);

	void join(UserDto userDto);

	void updateUser(String email, UserDto userDto);

	void updateConnectedId(String email, String connectedId);

	void updateBranchId(String email, Integer branchId);

	void withdrawUser(String email);
}
