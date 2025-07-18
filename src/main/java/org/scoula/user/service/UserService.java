package org.scoula.user.service;

import org.scoula.user.dto.UserDTO;

public interface UserService {
    UserDTO getUser(String email);
    void join(UserDTO userDTO);
}