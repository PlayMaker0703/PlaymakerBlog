package com.zyf.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.zyf.enums.AppHttpCodeEnum;
import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.dto.add.AddCategoryDto;
import com.zyf.pojo.dto.list.CategoryListDto;
import com.zyf.pojo.dto.edit.EditCategoryDto;
import com.zyf.pojo.entity.Category;
import com.zyf.pojo.vo.ExcelCategoryVo;
import com.zyf.service.CategoryService;
import com.zyf.utils.BeanCopyUtils;
import com.zyf.utils.WebUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory() {
        return categoryService.listAllCategory();
    }

    /**
     * 进行权限控制
     * @param response
     */
    @PreAuthorize("@permissionService.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xl" +
                    "sx", response);
            //获取需要导出的数据
            List<Category> categories = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categories, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    @GetMapping("/list")
    public ResponseResult list(CategoryListDto categoryListDto,Integer pageNum,Integer pageSize){
        return categoryService.selectCategoryPage(categoryListDto,pageNum,pageSize);
    }

    @PostMapping
    public ResponseResult addCategory(@RequestBody AddCategoryDto addCategoryDto){
        return categoryService.addCategory(addCategoryDto);
    }

    @GetMapping("/{categoryId}")
    public ResponseResult getInfo(@PathVariable("categoryId") Long categoryId){
        return categoryService.getInfo(categoryId);
    }

    @PutMapping
    public ResponseResult editCategory(@RequestBody EditCategoryDto editCategoryDto){
        return categoryService.editCategory(editCategoryDto);
    }

    @DeleteMapping("/{categoryIds}")
    public ResponseResult deleteCategory(@PathVariable("categoryIds") List<Long> categoryIds){
        return categoryService.deleteCategory(categoryIds);
    }

}
