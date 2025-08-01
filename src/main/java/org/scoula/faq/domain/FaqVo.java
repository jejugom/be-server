package org.scoula.faq.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaqVo {
	private int faqId;
	private String category;
	private String title;
	private String content;
}
