package org.scoula.news.controller;

import java.util.List;
import org.scoula.news.domain.NewsVo;
import org.scoula.news.dto.NewsDto;
import org.scoula.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news")
public class NewsController {

  @Autowired
  private NewsService newsService;


  @PostMapping("/crawl")
  public ResponseEntity<String> crawlNewsManually() {
    List<Integer> updatedCategories = newsService.crawlAndSaveNews();
    if (updatedCategories.isEmpty()) {
      return ResponseEntity.ok("No new categories were updated.");
    }
    //  변경된 카테고리 번호만 반환
    return ResponseEntity.ok("Change category is: " + updatedCategories);
  }

  //  가져온 뉴스 목록을 반환하는 API
  @GetMapping("")
  public ResponseEntity<List<NewsDto>> getNews() {
    return ResponseEntity.ok(newsService.getAllNews());
  }
}