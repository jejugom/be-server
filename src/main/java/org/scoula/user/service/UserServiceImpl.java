package org.scoula.user.service;

import java.text.SimpleDateFormat;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.scoula.user.domain.UserVo;
import org.scoula.user.dto.UserDto;
import org.scoula.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;

	@Override
	public UserDto getUser(String email) {
		UserVo userVO = Optional.ofNullable(userMapper.findByEmail(email))
			.orElseThrow(NoSuchElementException::new);
		return UserDto.of(userVO);
	}

	@Transactional
	@Override
	public void join(UserDto userDto) {
		UserVo userVO = userDto.toVo();
		userMapper.save(userVO);
	}

	@Transactional
	@Override
	public void updateConnectedId(String email, String connectedId) {
		userMapper.updateConnectedId(email, connectedId);
	}

	@Override
	public void updateBranchName(String email, String branchName) {
		userMapper.updateBranchName(email, branchName);
	}

	@Override
	public String getUserName(String email) {
		return Optional.ofNullable(userMapper.findByEmail(email))
			.map(UserVo::getUserName)
			.orElse(null);
	}

	@Override
	public String getUserPhone(String email) {
		return Optional.ofNullable(userMapper.findByEmail(email))
			.map(UserVo::getUserPhone)
			.orElse(null);
	}

	@Override
	public String getBirth(String email) {
		return Optional.ofNullable(userMapper.findByEmail(email))
			.map(UserVo::getBirth)
			.map(date -> new SimpleDateFormat("yyyy-MM-dd").format(date))
			.orElse(null);
	}

	@Override
	public String getBranchName(String email) {
		return Optional.ofNullable(userMapper.findByEmail(email))
			.map(UserVo::getBranchName)
			.orElse(null);
	}

	@Override
	public String getConnectedId(String email) {
		return Optional.ofNullable(userMapper.findByEmail(email))
			.map(UserVo::getConnectedId)
			.orElse(null);
	}

}
