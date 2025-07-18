package org.scoula.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.user.domain.UserVO;
import org.scoula.user.dto.UserDTO;
import org.scoula.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public UserDTO getUser(String email) {
        UserVO userVO = Optional.ofNullable(userMapper.findByEmail(email))
                .orElseThrow(NoSuchElementException::new);
        return UserDTO.of(userVO);
    }

    @Transactional
    @Override
    public void join(UserDTO userDTO) {
        UserVO userVO = userDTO.toVO();
        userMapper.insertUser(userVO);
    }
}
