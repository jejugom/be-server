package org.scoula.user.mapper;

import org.scoula.user.domain.UserVO;

public interface UserMapper {
    UserVO findByEmail(String email);
    int insertUser(UserVO user);
}
