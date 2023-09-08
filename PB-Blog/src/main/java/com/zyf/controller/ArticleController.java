package com.zyf.controller;

import com.zyf.pojo.ResponseResult;
import com.zyf.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/article")
@Api(tags = "文章接口")
public class ArticleController {
    @Resource
    private ArticleService articleService;

    @GetMapping("/hotArticleList")
    @ApiOperation(value = "热门文章", notes = "获取热门文章列表")
    public ResponseResult hotArticleList() {
        //查询热门文章封装成ResponseResult返回
        return articleService.hotArticleList();
    }

    @GetMapping("/articleList")
    @ApiOperation(value = "获取文章", notes = "分页获取文章列表")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "pageNum", value = "页码"),
                    @ApiImplicitParam(name = "pageSize", value = "每页大小"),
                    @ApiImplicitParam(name = "categoryId", value = "分类id")
            }
    )
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        return articleService.articleList(pageNum, pageSize, categoryId);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询文章", notes = "根据文章id查询文章")
    @ApiImplicitParam(name = "id", value = "文章id")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id) {
        return articleService.getArticleDetail(id);
    }

    @PutMapping("updateViewCount/{id}")
    @ApiOperation(value = "更新浏览量", notes = "根据文章id更新对应的浏览量")
    @ApiImplicitParam(name = "id", value = "文章id")
    public ResponseResult updateViewCount(@PathVariable("id") Long id) {
        return articleService.updateViewCount(id);
    }
}
