package com.zyf.job;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zyf.pojo.entity.Article;
import com.zyf.service.ArticleService;
import com.zyf.utils.RedisCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {
    @Resource
    private RedisCache redisCache;

    @Resource
    private ArticleService articleService;

    @Scheduled(cron = "0/59 * * * * ?")
    public void updateViewCount() {
        //获取redis中的浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap("article:viewCount");
        //对map集合进行转换处理
        List<Article> articles = viewCountMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        //更新到redis中
        for (Article article : articles) {
            LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Article::getId, article.getId());
            updateWrapper.set(Article::getViewCount, article.getViewCount());
            articleService.update(updateWrapper);
        }
    }
}
