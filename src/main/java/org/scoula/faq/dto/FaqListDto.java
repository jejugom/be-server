package org.scoula.faq.dto;

import org.scoula.faq.domain.FaqVo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FaqListDto {
	private int faqId;
	private String category;
	private String title;

	public static FaqListDto from(FaqVo faq) {
		return FaqListDto.builder()
			.faqId(faq.getFaqId())
			.category(faq.getCategory())
			.title(faq.getTitle())
			.build();
	}
}
