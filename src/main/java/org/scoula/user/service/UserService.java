package org.scoula.user.service;

import org.scoula.user.dto.MyPageResponseDto;
import org.scoula.user.dto.UserDto;

public interface UserService {
	UserDto getUser(String email);

	void join(UserDto userDto);

	void updateUser(String email, UserDto userDto);

	void updateConnectedId(String email, String connectedId);

	void updateBranchId(String email, Integer branchId);

	void withdrawUser(String email);

	/**
	 * 사용자 ID를 기반으로 마이페이지 데이터를 조회하여 DTO로 반환합니다.
	 * @param email 사용자의 고유 식별자 (예: 이메일)
	 * @return MyPageResponseDto
	 */
	MyPageResponseDto getMyPageData(String email);
}
