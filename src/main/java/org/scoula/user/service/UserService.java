package org.scoula.user.service;

import org.scoula.user.dto.UserDTO;
import org.scoula.user.dto.LoginDTO;

public interface UserService {
    UserDTO getUser(String email);
    void join(UserDTO userDTO);
    UserDTO login(LoginDTO loginDTO);
}