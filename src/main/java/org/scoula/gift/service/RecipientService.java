package org.scoula.gift.service;

import org.scoula.gift.dto.GiftPageResponseDto;
import org.scoula.gift.dto.RecipientRequestDto;
import org.scoula.gift.dto.RecipientResponseDto;

public interface RecipientService {
	// 수증자 생성
	RecipientResponseDto createRecipient(RecipientRequestDto requestDto, String email);

	// ID와 이메일로 특정 수증자 조회
	RecipientResponseDto findRecipientByIdAndEmail(Integer recipientId, String email);

	// 수증자 정보 수정
	RecipientResponseDto updateRecipient(Integer recipientId, RecipientRequestDto requestDto, String email);

	// 수증자 정보 삭제
	boolean deleteRecipient(Integer recipientId, String email);

	GiftPageResponseDto getGiftPageData(String email);
}
