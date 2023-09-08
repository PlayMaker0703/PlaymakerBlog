package com.zyf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyf.constants.SystemConstants;
import com.zyf.mapper.CategoryMapper;
import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.dto.add.AddCategoryDto;
import com.zyf.pojo.dto.list.CategoryListDto;
import com.zyf.pojo.dto.edit.EditCategoryDto;
import com.zyf.pojo.entity.Article;
import com.zyf.pojo.entity.Category;
import com.zyf.pojo.vo.CategoryVo;
import com.zyf.pojo.vo.PageVo;
import com.zyf.service.ArticleService;
import com.zyf.service.CategoryService;
import com.zyf.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-08-29 19:36:25
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Resource
    ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        //查询文章表中 状态为已发布的文章
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleWrapper);
        //获取文章分类id 并且去重
        Set<Long> categoryIds = articleList.stream()
                .map(Article::getCategoryId)
                .collect(Collectors.toSet());

        //查询分类表
        List<Category> categories = listByIds(categoryIds);
        categories = categories.stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus, SystemConstants.NORMAL);
        List<Category> list = list(queryWrapper);
        List<CategoryVo> categoryVoList = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return ResponseResult.okResult(categoryVoList);
    }

    @Override
    public ResponseResult selectCategoryPage(CategoryListDto categoryListDto, Integer pageNum, Integer pageSize) {
        Category category = BeanCopyUtils.copyBean(categoryListDto, Category.class);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //模糊查询
        queryWrapper.like(StringUtils.hasText(category.getName()), Category::getName, category.getName());
        queryWrapper.eq(Objects.nonNull(category.getStatus()), Category::getStatus, category.getStatus());

        Page<Category> categoryPage = new Page<>();
        categoryPage.setCurrent(pageNum);
        categoryPage.setSize(pageSize);
        page(categoryPage, queryWrapper);

        PageVo pageVo = new PageVo(categoryPage.getRecords(), categoryPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addCategory(AddCategoryDto addCategoryDto) {
        Category category = BeanCopyUtils.copyBean(addCategoryDto, Category.class);
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getInfo(Long categoryId) {
        Category category = getById(categoryId);
        CategoryVo categoryVo = BeanCopyUtils.copyBean(category, CategoryVo.class);
        return ResponseResult.okResult(categoryVo);
    }

    @Override
    public ResponseResult editCategory(EditCategoryDto editCategoryDto) {
        Category category = BeanCopyUtils.copyBean(editCategoryDto, Category.class);
        updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteCategory(List<Long> categoryIds) {
        removeByIds(categoryIds);
        return ResponseResult.okResult();
    }

}
