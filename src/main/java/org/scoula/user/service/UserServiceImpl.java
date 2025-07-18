package org.scoula.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.user.domain.UserVO;
import org.scoula.user.dto.UserDTO;
import org.scoula.user.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

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
        // 비밀번호 암호화 (만약 UserDTO에 password 필드가 있다면)
        // userVO.setPassword(passwordEncoder.encode(userVO.getPassword()));
        userMapper.insertUser(userVO);
    }
}
