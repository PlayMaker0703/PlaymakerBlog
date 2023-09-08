package com.zyf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.dto.add.AddCategoryDto;
import com.zyf.pojo.dto.list.CategoryListDto;
import com.zyf.pojo.dto.edit.EditCategoryDto;
import com.zyf.pojo.entity.Category;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-08-29 19:36:25
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();


    ResponseResult listAllCategory();

    ResponseResult selectCategoryPage(CategoryListDto categoryListDto, Integer pageNum, Integer pageSize);

    ResponseResult addCategory(AddCategoryDto addCategoryDto);

    ResponseResult getInfo(Long categoryId);

    ResponseResult editCategory(EditCategoryDto editCategoryDto);

    ResponseResult deleteCategory(List<Long> categoryIds);

}

