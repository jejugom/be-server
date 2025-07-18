package org.scoula.faq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.faq.domain.FaqVO;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaqDTO {
    private Integer id;
    private String category;
    private String title;
    private String content;
    private Date date;

    public static FaqDTO of(FaqVO faq) {
        return FaqDTO.builder()
                .id(faq.getId())
                .category(faq.getCategory())
                .title(faq.getTitle())
                .content(faq.getContent())
                .date(faq.getDate())
                .build();
    }

    public FaqVO toVO() {
        return FaqVO.builder()
                .id(id)
                .category(category)
                .title(title)
                .content(content)
                .date(date)
                .build();
    }
}
