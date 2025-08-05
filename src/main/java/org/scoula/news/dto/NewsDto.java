package org.scoula.news.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsDto {

  private Integer category;
  private String title;
  private String link;
  private String date;
  private String summary;
}