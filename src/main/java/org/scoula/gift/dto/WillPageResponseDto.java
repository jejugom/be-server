package org.scoula.gift.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 유언장 템플릿 페이지에 필요한 사용자 정보를 전달하는 DTO
 */
@Getter
@Builder // 빌더 패턴을 사용하여 객체를 안전하고 명확하게 생성
public class WillPageResponseDto {

	private final String email;
	private final String name;
	private final String birth;

}
