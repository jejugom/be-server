package org.scoula.user.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.scoula.asset.mapper.AssetStatusMapper;
import org.scoula.auth.mapper.RefreshTokenMapper;
import org.scoula.booking.mapper.BookingMapper;
import org.scoula.user.domain.UserVo;
import org.scoula.user.dto.UserDto;
import org.scoula.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * 사용자 관련 서비스 구현체입니다.
 * 데이터베이스 연동을 위해 여러 Mapper를 주입받습니다.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;
	private final RefreshTokenMapper refreshTokenMapper;
	private final BookingMapper bookingMapper;
	private final AssetStatusMapper assetStatusMapper;

	/**
	 * 이메일로 사용자 정보를 조회합니다.
	 *
	 * @param email 사용자 이메일
	 * @return 사용자 정보를 담은 DTO
	 * @throws NoSuchElementException 사용자가 존재하지 않을 경우
	 */
	@Override
	public UserDto getUser(String email) {
		UserVo userVO = Optional.ofNullable(userMapper.findByEmail(email))
			.orElseThrow(NoSuchElementException::new);
		return UserDto.of(userVO);
	}

	/**
	 * 신규 사용자를 저장합니다.
	 *
	 * @param userDto 저장할 사용자 정보
	 */
	@Transactional
	@Override
	public void join(UserDto userDto) {
		UserVo userVO = userDto.toVo();
		userMapper.save(userVO);
	}

	/**
	 * 이메일 기준으로 사용자의 정보를 수정합니다.
	 *
	 * @param email   수정할 대상 사용자의 이메일
	 * @param userDto 수정할 사용자 정보
	 * @throws NoSuchElementException 사용자가 존재하지 않을 경우
	 */
	@Transactional
	@Override
	public void updateUser(String email, UserDto userDto) {
		UserVo user = Optional.ofNullable(userMapper.findByEmail(email))
			.orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

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

	/**
	 * 사용자의 ConnectedId를 수정합니다.
	 *
	 * @param email       사용자 이메일
	 * @param connectedId 새로운 ConnectedId
	 */
	@Transactional
	@Override
	public void updateConnectedId(String email, String connectedId) {
		userMapper.updateConnectedId(email, connectedId);
	}

	/**
	 * 사용자의 BranchId를 수정합니다.
	 *
	 * @param email    사용자 이메일
	 * @param branchId 새로운 BranchId
	 */
	@Transactional
	@Override
	public void updateBranchId(String email, Integer branchId) {
		userMapper.updateBranchId(email, branchId);
	}

	/**
	 * 사용자의 모든 정보를 삭제합니다. (회원 탈퇴)
	 * 자식 테이블(리프레시 토큰, 예약, 자산 상태)부터 삭제 후
	 * 마지막으로 사용자 테이블 삭제.
	 *
	 * @param email 탈퇴할 사용자 이메일
	 * @throws NoSuchElementException 사용자가 존재하지 않을 경우
	 */
	@Transactional
	@Override
	public void withdrawUser(String email) {
		refreshTokenMapper.deleteByEmail(email);
		bookingMapper.deleteByEmail(email);
		assetStatusMapper.deleteByEmail(email);

		int affectedRows = userMapper.deleteByEmail(email);
		if (affectedRows == 0) {
			throw new NoSuchElementException("User not found with email: " + email);
		}
	}
}
