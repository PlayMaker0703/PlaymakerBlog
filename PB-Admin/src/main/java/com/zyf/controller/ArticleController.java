package com.zyf.controller;

import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.dto.add.AddArticleDto;
import com.zyf.pojo.dto.ArticleDto;
import com.zyf.pojo.dto.list.ArticleListDto;
import com.zyf.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Resource
    ArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto article) {
        return articleService.addArticle(article);
    }

    @GetMapping("/list")
    public ResponseResult list(ArticleListDto articleListDto, Integer pageNum, Integer pageSize) {
        return articleService.selectArticlePage(articleListDto, pageNum, pageSize);
    }

    @GetMapping("/{id}")
    public ResponseResult getInfo(@PathVariable("id") Long id) {
        return articleService.getInfo(id);
    }

    @PutMapping
    public ResponseResult edit(@RequestBody ArticleDto articleDto) {
        return articleService.editArticle(articleDto);
    }

    @DeleteMapping("/{articleIds}")
    public ResponseResult delete(@PathVariable("articleIds") List<Long> articleIds) {
        return articleService.deleteArticle(articleIds);
    }

}
