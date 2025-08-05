package org.scoula.news.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.news.domain.NewsVo;

@Mapper
public interface NewsMapper {

  NewsVo findByCategoryAndTitle(@Param("category") int category, @Param("title") String title);

  NewsVo findByCategory(Integer category);

  void insertNews(NewsVo news);

  void updateNews(NewsVo news);

  List<NewsVo> findAll();
}