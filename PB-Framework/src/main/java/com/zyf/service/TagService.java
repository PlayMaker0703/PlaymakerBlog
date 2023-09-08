package com.zyf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.dto.edit.EditTagDto;
import com.zyf.pojo.dto.list.TagListDto;
import com.zyf.pojo.entity.Tag;
import com.zyf.pojo.vo.PageVo;

import java.util.List;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-09-04 14:04:42
 */
public interface TagService extends IService<Tag> {
    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(Tag tag);

    ResponseResult deleteTag(List<Long> tagIds);

    ResponseResult editTag(EditTagDto editTagDto);

    ResponseResult getTagById(Long tagId);

    ResponseResult listAllTag();


}

