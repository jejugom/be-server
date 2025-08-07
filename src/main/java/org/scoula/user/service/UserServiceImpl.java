package org.scoula.user.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.scoula.asset.dto.AssetStatusSummaryDto;
import org.scoula.asset.mapper.AssetStatusMapper;
import org.scoula.auth.mapper.RefreshTokenMapper;
import org.scoula.booking.domain.BookingVo;
import org.scoula.booking.dto.BookingDto;
import org.scoula.booking.mapper.BookingMapper;
import org.scoula.user.domain.UserVo;
import org.scoula.user.dto.MyPageResponseDto;
import org.scoula.user.dto.UserDto;
import org.scoula.user.dto.UserGraphDto;
import org.scoula.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * UserService의 구현 클래스.
 * 여러 Mapper를 주입받아 사용자 관련 비즈니스 로직을 수행합니다.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserAssetUpdater {

	private final UserMapper userMapper;
	private final RefreshTokenMapper refreshTokenMapper;
	private final BookingMapper bookingMapper;
	private final AssetStatusMapper assetStatusMapper;

	@Override
	public UserDto getUser(String email) {
		UserVo userVO = Optional.ofNullable(userMapper.findByEmail(email))
			.orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다: " + email));
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
			.orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다: " + email));

		// DTO의 정보로 기존 VO 객체의 상태를 업데이트
		user.setUserName(userDto.getUserName());
		user.setUserPhone(userDto.getUserPhone());
		user.setBirth(userDto.getBirth());
		user.setConnectedId(userDto.getConnectedId());
		user.setBranchId(userDto.getBranchId());
		user.setAsset(userDto.getAsset());
		user.setFilename1(userDto.getFilename1());
		user.setFilename2(userDto.getFilename2());
		user.setTendency(userDto.getTendency());
		user.setAssetProportion(userDto.getAssetProportion());

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

	@Transactional
	@Override
	public void withdrawUser(String email) {
		// 사용자와 관련된 모든 데이터를 순서대로 삭제
		refreshTokenMapper.deleteByEmail(email);
		bookingMapper.deleteByEmail(email);
		assetStatusMapper.deleteByEmail(email);

		int affectedRows = userMapper.deleteByEmail(email);
		if (affectedRows == 0) { // 삭제된 사용자가 없으면 예외 발생
			throw new NoSuchElementException("User not found with email: " + email);
		}
	}

	/**
	 * 마이페이지에 필요한 사용자 정보, 자산 현황, 예약 내역을 조합하여 반환합니다.
	 * @param email 조회할 사용자의 이메일
	 * @return 마이페이지 데이터 DTO
	 */
	@Override
	public MyPageResponseDto getMyPageData(String email) {
		// 1. 사용자 정보 조회
		UserVo userVo = Optional.ofNullable(userMapper.findByEmail(email))
			.orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다: " + email));

		// 2. 자산 현황 요약 목록 조회
		List<AssetStatusSummaryDto> assetList = assetStatusMapper.findAssetStatusSummaryByEmail(email)
			.stream()
			.map(AssetStatusSummaryDto::of)
			.collect(Collectors.toList());

		// 3. 다가오는 예약 내역 목록 조회
		List<BookingVo> bookingVos = bookingMapper.findUpcomingByUserEmail(email);
		List<BookingDto> bookingDtos = bookingVos.stream()
			.map(BookingDto::of)
			.collect(Collectors.toList());

		// 4. 자산 상위 백분위 계산 로직
		Double percentile = calculateAssetPercentile(userVo.getAsset());

		// 5. 최종 DTO 조립
		UserGraphDto userInfoDto = UserGraphDto.builder()
			.userName(userVo.getUserName())
			.assetStatus(assetList)
			.build();

		return MyPageResponseDto.builder()
			.userInfo(userInfoDto)
			.bookingInfo(bookingDtos)
			.assetPercentile(percentile) // 계산된 백분위 값 추가
			.build();
	}

	/**
	 * 자산의 상위 백분위를 계산하는 private 헬퍼 메소드
	 * @param myAsset 현재 사용자의 자산
	 * @return 소수점 첫째 자리까지 계산된 상위 백분위 (%), 자산이 없거나 사용자가 적을 경우 null 반환
	 */
	private Double calculateAssetPercentile(Long myAsset) {
		// 사용자의 자산 정보가 없으면 계산 불가
		if (myAsset == null) {
			return null;
		}

		// 자산 정보가 있는 전체 사용자 수 조회
		long totalUsers = userMapper.countAllUsersWithAsset();

		// 비교 대상 사용자가 1명 이하면 백분위 의미 없음
		if (totalUsers <= 1) {
			return 100.0; // 혼자일 경우 상위 100%로 표시
		}

		// 나보다 자산이 많은 사용자 수 조회
		long usersWithMoreAsset = userMapper.countUsersWithMoreAsset(myAsset);

		// 상위 백분위 계산: (나의 등수 / 전체 인원) * 100
		double myRank = (double)(usersWithMoreAsset + 1);
		double rawPercentile = (myRank / totalUsers) * 100.0;

		// 소수점 첫째 자리까지 반올림하여 반환
		return new BigDecimal(rawPercentile)
			.setScale(1, RoundingMode.HALF_UP)
			.doubleValue();
	}

	/**
	 * 특정 사용자의 총 자산을 업데이트합니다.
	 * @param email 사용자 이메일
	 * @param amount 증감할 금액
	 */
	@Transactional
	@Override
	public void updateUserAsset(String email, long amount) {
		UserDto userDto = getUser(email);
		userDto.setAsset(userDto.getAsset() + amount);
		updateUser(email, userDto);
	}
}
