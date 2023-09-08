package com.zyf.runner;

import com.zyf.mapper.ArticleMapper;
import com.zyf.pojo.entity.Article;
import com.zyf.utils.RedisCache;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {
    @Resource
    private ArticleMapper articleMapper;

    @Resource
    RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        //查询博客信息 id viewCount
        List<Article> articleList = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articleList.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(), article -> article.getViewCount().intValue()));
        //存储到redis中 id为key viewCount 为value
        redisCache.setCacheMap("article:viewCount", viewCountMap);
    }
}
