package com.zyf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.dto.add.AddArticleDto;
import com.zyf.pojo.dto.ArticleDto;
import com.zyf.pojo.dto.list.ArticleListDto;
import com.zyf.pojo.entity.Article;

import java.util.List;

public interface ArticleService extends IService<Article> {
    //热门文章
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult addArticle(AddArticleDto articleDto);

    ResponseResult selectArticlePage(ArticleListDto articleListDto, Integer pageNum, Integer pageSize);

    ResponseResult getInfo(Long id);

    ResponseResult editArticle(ArticleDto articleDto);

    ResponseResult deleteArticle(List<Long> articleIds);
}
