package org.scoula.gift.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.scoula.asset.dto.AssetStatusSummaryDto;
import org.scoula.asset.service.AssetStatusService;
import org.scoula.gift.domain.RecipientVo;
import org.scoula.gift.dto.GiftPageResponseDto;
import org.scoula.gift.dto.RecipientRequestDto;
import org.scoula.gift.dto.RecipientResponseDto;
import org.scoula.gift.mapper.RecipientMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipientServiceImpl implements RecipientService {

	private final RecipientMapper recipientMapper;
	private final AssetStatusService assetStatusService;

	/**
	 * 새로운 수증자 정보를 생성합니다.
	 *
	 * @param requestDto 생성할 수증자 정보가 담긴 DTO
	 * @param email      현재 인증된 사용자의 이메일
	 * @return 생성된 수증자 정보가 담긴 응답 DTO
	 * @throws IllegalStateException 데이터베이스에 정상적으로 생성된 후 조회가 실패하는 경우 발생
	 */
	@Override
	public RecipientResponseDto createRecipient(RecipientRequestDto requestDto, String email) {
		RecipientVo vo = requestDto.toVo(email);
		recipientMapper.insertRecipient(vo);

		// 생성 후 즉시 조회하여 데이터 무결성 확인
		RecipientVo createdVo = recipientMapper.findById(vo.getRecipientId());
		if (createdVo == null) {
			// 이 경우는 거의 없지만, 발생 시 서버 내부 문제임
			throw new IllegalStateException("수증자 정보 생성 후 데이터를 조회할 수 없습니다.");
		}
		return RecipientResponseDto.from(createdVo);
	}

	/**
	 * 특정 수증자 정보를 조회합니다.
	 * 해당 수증자가 존재하지 않거나, 현재 사용자의 소유가 아닐 경우 예외를 발생시킵니다.
	 *
	 * @param recipientId 조회할 수증자의 ID
	 * @param email       현재 인증된 사용자의 이메일
	 * @return 조회된 수증자 정보가 담긴 응답 DTO
	 * @throws NoSuchElementException 요청한 ID의 수증자가 없거나 접근 권한이 없을 경우 발생
	 */
	@Override
	public RecipientResponseDto findRecipientByIdAndEmail(Integer recipientId, String email) {
		RecipientVo vo = recipientMapper.findByIdAndEmail(recipientId, email);
		if (vo == null) {
			// null을 반환하는 대신, 예외를 던져 GlobalExceptionHandler가 404로 처리하도록 함
			throw new NoSuchElementException("ID " + recipientId + "에 해당하는 수증자를 찾을 수 없거나 접근 권한이 없습니다.");
		}
		return RecipientResponseDto.from(vo);
	}

	/**
	 * 특정 수증자 정보를 수정합니다.
	 * 수정 전, 해당 수증자가 존재하는지와 현재 사용자의 소유인지를 먼저 검증합니다.
	 *
	 * @param recipientId 수정할 수증자의 ID
	 * @param requestDto  수정할 내용이 담긴 DTO
	 * @param email       현재 인증된 사용자의 이메일
	 * @return 수정된 수증자 정보가 담긴 응답 DTO
	 * @throws NoSuchElementException 수정할 대상이 없거나 접근 권한이 없을 경우 발생
	 */
	@Override
	public RecipientResponseDto updateRecipient(Integer recipientId, RecipientRequestDto requestDto, String email) {
		// 1. 수정할 데이터가 존재하는지, 현재 사용자의 소유인지 확인 (없으면 여기서 예외 발생)
		findRecipientByIdAndEmail(recipientId, email);

		// 2. 수정할 내용으로 VO 객체 생성
		RecipientVo voToUpdate = requestDto.toVo(email);
		voToUpdate.setRecipientId(recipientId);

		// 3. DB 업데이트
		recipientMapper.updateRecipient(voToUpdate);

		// 4. 업데이트된 정보를 DTO로 변환하여 반환
		return RecipientResponseDto.from(voToUpdate);
	}

	/**
	 * 특정 수증자 정보를 삭제합니다.
	 * 삭제 전, 해당 수증자가 존재하는지와 현재 사용자의 소유인지를 먼저 검증합니다.
	 *
	 * @param recipientId 삭제할 수증자의 ID
	 * @param email       현재 인증된 사용자의 이메일
	 * @return 삭제 성공 여부 (true: 성공, false: 실패)
	 * @throws NoSuchElementException 삭제할 대상이 없거나 접근 권한이 없을 경우 발생
	 */
	@Override
	public boolean deleteRecipient(Integer recipientId, String email) {
		// 1. 삭제할 데이터가 존재하는지, 현재 사용자의 소유인지 확인 (없으면 여기서 예외 발생)
		findRecipientByIdAndEmail(recipientId, email);

		// 2. DB에서 삭제
		int affectedRows = recipientMapper.deleteById(recipientId);
		return affectedRows > 0;
	}

	/**
	 * 증여 페이지에 필요한 전체 데이터(수증자 목록, 자산 요약)를 조회합니다.
	 *
	 * @param email 현재 인증된 사용자의 이메일
	 * @return 수증자 목록과 자산 요약 정보가 담긴 최종 응답 DTO
	 */
	@Override
	public GiftPageResponseDto getGiftPageData(String email) {
		List<RecipientVo> recipientVoList = recipientMapper.findByEmail(email);
		List<RecipientResponseDto> recipientDtoList = recipientVoList.stream()
			.map(RecipientResponseDto::from)
			.collect(Collectors.toList());

		List<AssetStatusSummaryDto> assetSummaryDtoList = assetStatusService.getAssetStatusSummaryByEmail(email);

		return GiftPageResponseDto.builder()
			.recipients(recipientDtoList)
			.assetSummary(assetSummaryDtoList)
			.build();
	}
}
