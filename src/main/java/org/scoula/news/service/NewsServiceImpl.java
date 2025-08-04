package org.scoula.news.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.scoula.news.domain.NewsVo;
import org.scoula.news.dto.NewsDto;
import org.scoula.news.mapper.NewsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class NewsServiceImpl implements NewsService {

  private static final Map<Integer, String> CATEGORY_KEYWORDS = Map.of(
      1, "예금",
      2, "적금",
      3, "주택담보",
      4, "금",
      5, "펀드"
  );
  @Autowired
  private NewsMapper newsMapper;

  @Override
  public List<Integer> crawlAndSaveNews() {
    List<Integer> updatedCategories = new ArrayList<>();
    try {
      Document doc = Jsoup.connect(
          "https://www.newswire.co.kr/?md=A10&act=article&no=199&perpage=100").get();
      Elements newsList = doc.select(".news-column");

      // 카테고리별 키워드 리스트 (복수 키워드)
      Map<Integer, List<String>> CATEGORY_KEYWORDS = Map.of(
          1, List.of("예금"),
          2, List.of("적금"),
          3, List.of("주택", "담보", "대출"),
          4, List.of("\\b금\\b", "순금", "골드바", "금 투자", "금 시세"),
          5, List.of("펀드")
      );

      for (Map.Entry<Integer, List<String>> entry : CATEGORY_KEYWORDS.entrySet()) {
        Integer category = entry.getKey();
        List<String> keywords = entry.getValue();

        for (Element news : newsList) {
          String title = news.select("h5 a").text();

          boolean isMatch = false;

          // 5번 카테고리는 2개 이상 키워드가 포함돼야 true
          if (category == 3) {
            long count = keywords.stream().filter(title::contains).count();
            if (count >= 1) {
              isMatch = true;
            }
          } else if (category == 4) { // 금 관련 정규식 처리
            for (String keyword : keywords) {
              if (title.matches(".*" + keyword + ".*")) {
                isMatch = true;
                break;
              }
            }
          } else { // 나머지는 단순 포함
            for (String keyword : keywords) {
              if (title.contains(keyword)) {
                isMatch = true;
                break;
              }
            }
          }

          if (!isMatch) {
            continue;
          }

          String link = news.select("h5 a").attr("href");
          String summary = news.select(".content a").text();
          String date = news.select(".info .mdate").text();

          NewsVo existing = newsMapper.findByCategory(category);
          NewsVo newNews = new NewsVo(null, category, title, link, date, summary, null);

          if (existing == null) {
            newsMapper.insertNews(newNews);
            updatedCategories.add(category);
          } else if (!existing.getTitle().equals(title)) {
            newsMapper.updateNews(newNews);
            updatedCategories.add(category);
          }

          break; // 다음 카테고리로 이동
        }
      }

    } catch (IOException e) {
      log.error("IOException occurred while crawling and saving news", e);
    }
    return updatedCategories;
  }

  @Override
  public List<NewsDto> getAllNews() {
    return newsMapper.findAll().stream()
        .map(n -> new NewsDto(n.getCategory(), n.getTitle(), n.getLink(), n.getDate(),
            n.getSummary()))
        .collect(Collectors.toList());
  }
}