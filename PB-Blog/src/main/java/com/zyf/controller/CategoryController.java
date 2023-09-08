package com.zyf.controller;

import com.zyf.pojo.ResponseResult;
import com.zyf.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/category")
@Api(tags = "分类接口")
public class CategoryController {
    @Resource
    CategoryService categoryService;

    @GetMapping("/getCategoryList")
    @ApiOperation(value = "分类", notes = "获取分类列表")
    public ResponseResult getCategoryList() {
        return categoryService.getCategoryList();
    }
}
