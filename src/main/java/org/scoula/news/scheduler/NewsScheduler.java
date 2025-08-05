package org.scoula.news.scheduler;

import org.scoula.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class NewsScheduler {

  @Autowired
  private NewsService newsService;

  @Scheduled(cron = "0 0 12 * * *") // 매일 정오에 실행

//  스케줄링 작업
  public void scheduleNewsCrawling() {
    newsService.crawlAndSaveNews();
  }
}