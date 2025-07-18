package org.scoula.faq.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaqVO {
    private Integer id;
    private String category;
    private String title;
    private String content;
    private Date date;
}
