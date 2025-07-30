package org.scoula.gift.service;

import java.util.List;
import java.util.stream.Collectors;

import org.scoula.gift.domain.RecipientVo;
import org.scoula.gift.dto.RecipientListResponseDto;
import org.scoula.gift.dto.RecipientRequestDto;
import org.scoula.gift.dto.RecipientResponseDto;
import org.scoula.gift.mapper.RecipientMapper;
import org.springframework.stereotype.Service;

@Service
public class RecipientServiceImpl implements RecipientService {

	private final RecipientMapper recipientMapper;

	public RecipientServiceImpl(RecipientMapper recipientMapper) {
		this.recipientMapper = recipientMapper;
	}

	@Override
	public RecipientResponseDto createRecipient(RecipientRequestDto requestDto, String email) {
		// DTO를 VO로 변환
		RecipientVo vo = requestDto.toVo(email);
		// DB에 삽입
		recipientMapper.insertRecipient(vo);
		// 삽입된 데이터(auto-increment된 ID 포함)를 다시 조회하여 반환하는 것이 가장 정확함
		RecipientVo createdVo = recipientMapper.findById(vo.getRecipientId());
		// VO를 Response DTO로 변환하여 반환
		return RecipientResponseDto.from(createdVo);
	}

	@Override
	public RecipientListResponseDto findRecipientsByEmail(String email) {
		List<RecipientVo> voList = recipientMapper.findByEmail(email);
		// List<Vo>를 List<Dto>로 변환
		List<RecipientResponseDto> dtoList = voList.stream()
			.map(RecipientResponseDto::from)
			.collect(Collectors.toList());

		return new RecipientListResponseDto(dtoList, dtoList.size());
	}

	@Override
	public RecipientResponseDto findRecipientByIdAndEmail(Integer recipientId, String email) {
		RecipientVo vo = recipientMapper.findByIdAndEmail(recipientId, email);
		// 조회된 VO가 있으면 DTO로 변환, 없으면 null 반환
		return (vo != null) ? RecipientResponseDto.from(vo) : null;
	}

	@Override
	public RecipientResponseDto updateRecipient(Integer recipientId, RecipientRequestDto requestDto, String email) {
		// 1. 수정할 데이터가 현재 사용자의 소유인지 확인
		RecipientVo existingVo = recipientMapper.findByIdAndEmail(recipientId, email);
		if (existingVo == null) {
			// 대상이 없거나 권한이 없으면 null 반환
			return null;
		}

		// 2. 수정할 내용으로 VO 객체 생성
		RecipientVo voToUpdate = requestDto.toVo(email);
		voToUpdate.setRecipientId(recipientId); // PathVariable로 받은 ID를 설정

		// 3. DB 업데이트
		recipientMapper.updateRecipient(voToUpdate);

		// 4. 업데이트된 정보를 DTO로 변환하여 반환
		return RecipientResponseDto.from(voToUpdate);
	}

	@Override
	public boolean deleteRecipient(Integer recipientId, String email) {
		// 1. 삭제할 데이터가 현재 사용자의 소유인지 확인
		RecipientVo existingVo = recipientMapper.findByIdAndEmail(recipientId, email);
		if (existingVo == null) {
			// 대상이 없거나 권한이 없으면 false 반환
			return false;
		}

		// 2. DB에서 삭제
		int affectedRows = recipientMapper.deleteById(recipientId);
		return affectedRows > 0;
	}
}