package org.scoula.news.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsVo {

  private Long id;
  private Integer category;
  private String title;
  private String link;
  private String date;
  private String summary;
  private LocalDateTime createdAt;
}