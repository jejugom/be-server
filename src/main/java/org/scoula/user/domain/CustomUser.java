package org.scoula.user.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUser extends User {
    private UserVO user;

    public CustomUser(String username, String password,
                      Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public CustomUser(UserVO userVO) {
        super(userVO.getEmail(), userVO.getPassword(), userVO.getAuthList());
        this.user = userVO;
    }

    public UserVO getUser() {
        return user;
    }
}
