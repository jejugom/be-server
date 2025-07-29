package org.scoula.asset.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.scoula.asset.domain.AssetStatusVo;
import org.scoula.asset.dto.AssetStatusRequestDto;
import org.scoula.asset.dto.AssetStatusResponseDto;
import org.scoula.asset.dto.AssetStatusSummaryDto;
import org.scoula.asset.mapper.AssetStatusMapper;
import org.scoula.user.dto.UserDto;
import org.scoula.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssetStatusServiceImpl implements AssetStatusService {

	private final AssetStatusMapper assetStatusMapper;
	private final UserService userService;

	@Override
	public List<AssetStatusResponseDto> getAssetStatusByEmail(String email) {
		return assetStatusMapper.findAssetStatusByEmail(email).stream()
			.map(AssetStatusResponseDto::of)
			.collect(Collectors.toList());
	}

	// 노후 페이지에서 보여질 자산현황에 쓰일 메서드입니다.
	@Override
	public List<AssetStatusSummaryDto> getAssetStatusSummaryByEmail(String email) {
		return assetStatusMapper.findAssetStatusSummaryByEmail(email).stream()
			.map(AssetStatusSummaryDto::of)
			.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public void addAssetStatus(String email, AssetStatusRequestDto requestDto) {
		AssetStatusVo assetStatusVo = requestDto.toVo();
		assetStatusVo.setEmail(email);
		assetStatusMapper.insertAssetStatus(assetStatusVo);
		/***
		 사용자 테이블에 총 자산에 추가된 자산 금액 저장
		  userService를 호출해 requestDto 의 amount를 추가합니다.
		 */
		UserDto userDto = userService.getUser(email);
		userDto.setAsset(userDto.getAsset() + requestDto.getAmount());
		userService.updateUser(email,userDto);

	}

	@Override
	public void updateAssetStatus(Integer assetId, String email, AssetStatusRequestDto requestDto) {
		AssetStatusVo assetStatusVo = requestDto.toVo();
		assetStatusVo.setAssetId(assetId);
		assetStatusVo.setEmail(email);

		if (assetStatusMapper.updateAssetStatus(assetStatusVo) == 0) {
			throw new NoSuchElementException("목록 번호 오기입 / 권한이 없습니다.");
		}
	}

	@Override
	public void deleteAssetStatus(Integer assetId, String userEmail) {
		if (assetStatusMapper.deleteAssetStatus(assetId, userEmail) == 0) {
			throw new NoSuchElementException("해당 자산이 사용자 계정에 존재하지 않습니다. ");
		}
	}
}
