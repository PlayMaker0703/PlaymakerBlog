package com.zyf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyf.constants.SystemConstants;
import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.dto.add.AddArticleDto;
import com.zyf.pojo.dto.ArticleDto;
import com.zyf.pojo.dto.list.ArticleListDto;
import com.zyf.pojo.entity.Article;
import com.zyf.mapper.ArticleMapper;
import com.zyf.pojo.entity.ArticleTag;
import com.zyf.pojo.entity.Category;
import com.zyf.pojo.vo.*;
import com.zyf.service.ArticleService;
import com.zyf.service.ArticleTagService;
import com.zyf.service.CategoryService;
import com.zyf.utils.BeanCopyUtils;
import com.zyf.utils.RedisCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Resource
    private CategoryService categoryService;

    @Resource
    private RedisCache redisCache;

    @Resource
    private ArticleTagService articleTagService;

    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章 封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);//状态为0正式文章 为1是草稿
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多只查询10条
        Page<Article> page = new Page(1, 10);
        page(page, queryWrapper);

        List<Article> articles = page.getRecords();
        //bean拷贝
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        return ResponseResult.okResult(hotArticleVos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //如果有categoryId 查询时要和传入的相同
        queryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);
        //状态是正式发布的
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //对isTop降序
        queryWrapper.orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> articlePage = new Page<>(pageNum, pageSize);
        page(articlePage, queryWrapper);
        //查询categoryName
        List<Article> articles = articlePage.getRecords();
        //通过categoryId查询categoryName进行设置
        articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());


        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articlePage.getRecords(), ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos, articlePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis中获取浏览量ViewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        //查询之后转换为vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if (category != null) {
            articleDetailVo.setCategoryName(category.getName());
        }
        //返回
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应 id的浏览量
        redisCache.incrementCacheMapValue("article:viewCount", id.toString(), 1);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult addArticle(AddArticleDto articleDto) {
        //添加 博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);

        //得到文章和标签的对应关系表
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult selectArticlePage(ArticleListDto articleListDto, Integer pageNum, Integer pageSize) {
        Article article = BeanCopyUtils.copyBean(articleListDto, Article.class);

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //模糊查询
        queryWrapper.like(StringUtils.hasText(article.getTitle()), Article::getTitle, article.getTitle());
        queryWrapper.like(StringUtils.hasText(article.getSummary()), Article::getSummary, article.getSummary());//摘要

        Page<Article> articlePage = new Page<>();
        articlePage.setCurrent(pageNum);
        articlePage.setSize(pageSize);
        page(articlePage, queryWrapper);

        //转换成VO
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articlePage.getRecords(), ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos, articlePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getInfo(Long id) {
        Article article = getById(id);
        //获取关联标签
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, article.getId());
        List<ArticleTag> articleTags = articleTagService.list(queryWrapper);
        //封装tags信息
        List<Long> tags = articleTags.stream()
                .map(articleTag -> articleTag.getTagId())
                .collect(Collectors.toList());

        //封装vo返回
        ArticleVo articleVo = BeanCopyUtils.copyBean(article, ArticleVo.class);
        articleVo.setTags(tags);
        return ResponseResult.okResult(articleVo);
    }

    @Override
    public ResponseResult editArticle(ArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        //更新博客信息
        updateById(article);

        //删除原有的 标签和博客的关联
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, article.getId());
        articleTagService.remove(queryWrapper);

        //添加新的博客和标签的关联信息
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(articleDto.getId(), tagId))
                .collect(Collectors.toList());

        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteArticle(List<Long> articleIds) {
        removeByIds(articleIds);
        return ResponseResult.okResult();
    }


}
