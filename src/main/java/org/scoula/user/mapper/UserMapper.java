package org.scoula.user.mapper;

import org.scoula.user.domain.UserVo;

public interface UserMapper {
	UserVo findByEmail(String email);

	int save(UserVo user);

	int update(UserVo user);

	int delete(String email);
}
