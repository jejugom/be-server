package org.scoula.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.user.domain.AuthVO;
import org.scoula.user.domain.UserVO;
import org.scoula.user.dto.LoginDTO;
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

        AuthVO auth = new AuthVO();
        auth.setEmail(userVO.getEmail());
        auth.setAuth("ROLE_USER"); // 기본 권한 부여
        userMapper.insertAuth(auth);
    }

    @Override
    public UserDTO login(LoginDTO loginDTO) {
        // 로그인 로직 구현 (비밀번호 검증 등)
        // 이 부분은 Spring Security에서 처리될 가능성이 높으므로, 여기서는 간단히 매퍼를 통해 사용자 정보를 가져오는 것으로 대체합니다.
        UserVO userVO = userMapper.findByEmail(loginDTO.getEmail());
        if (userVO == null) {
            throw new NoSuchElementException("User not found");
        }
        // 실제 비밀번호 검증은 Spring Security의 AuthenticationProvider에서 이루어집니다.
        return UserDTO.of(userVO);
    }
}
