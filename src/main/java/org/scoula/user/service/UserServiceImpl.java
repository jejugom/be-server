package org.scoula.user.service;

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
	public void updateUser(String email, UserDto userDto) {
		UserVo user = Optional.ofNullable(userMapper.findByEmail(email))
			.orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

		// DTO의 필드를 VO에 업데이트
		user.setUserName(userDto.getUserName());
		user.setUserPhone(userDto.getUserPhone());
		user.setBirth(userDto.getBirth());
		user.setConnectedId(userDto.getConnectedId());
		user.setBranchId(userDto.getBranchId());
		user.setAsset(userDto.getAsset());
		user.setSegment(userDto.getSegment());
		user.setFilename1(userDto.getFilename1());
		user.setFilename2(userDto.getFilename2());
		user.setTendency(userDto.getTendency());
		user.setAssetProportion(userDto.getAssetProportion());
		user.setIncomeRange(userDto.getIncomeRange());
		user.setMartialStatus(userDto.getMartialStatus());

		userMapper.update(user);
	}

	@Transactional
	@Override
	public void updateConnectedId(String email, String connectedId) {
		userMapper.updateConnectedId(email, connectedId);
	}

	@Transactional
	@Override
	public void updateBranchId(String email, Integer branchId) {
		userMapper.updateBranchId(email, branchId);
	}
}
