package org.scoula.faq.dto;

import java.util.Date;

import org.scoula.faq.domain.FaqVo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaqDto {
	private int faqId;
	private String category;
	private String title;
	private String content;
	private Date date;

	public static FaqDto of(FaqVo faq) {
		return FaqDto.builder()
			.faqId(faq.getFaqId())
			.category(faq.getCategory())
			.title(faq.getTitle())
			.content(faq.getContent())
			.date(faq.getDate())
			.build();
	}

	public FaqVo toVo() {
		return FaqVo.builder()
			.faqId(faqId)
			.category(category)
			.title(title)
			.content(content)
			.date(date)
			.build();
	}
}
