package com.zyf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyf.enums.AppHttpCodeEnum;
import com.zyf.exception.SystemException;
import com.zyf.mapper.TagMapper;
import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.dto.edit.EditTagDto;
import com.zyf.pojo.dto.list.TagListDto;
import com.zyf.pojo.entity.Tag;
import com.zyf.pojo.vo.PageVo;
import com.zyf.pojo.vo.TagVo;
import com.zyf.service.TagService;
import com.zyf.utils.BeanCopyUtils;
import com.zyf.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        //分页查询
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()), Tag::getName, tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()), Tag::getRemark, tagListDto.getRemark());

        Page<Tag> tagPage = new Page<>();
        tagPage.setCurrent(pageNum);
        tagPage.setSize(pageSize);
        page(tagPage, queryWrapper);

        //封装数据返回
        PageVo pageVo = new PageVo(tagPage.getRecords(), tagPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(Tag tag) {
        //判断是否存在
        if (!StringUtils.hasText(tag.getName())) {
            throw new SystemException(AppHttpCodeEnum.TAG_NOT_NULL);
        }
        if (tagNameExist(tag.getName())) {
            throw new SystemException(AppHttpCodeEnum.TAG_EXIST);
        }
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(List<Long> tagIds) {
        removeByIds(tagIds);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult editTag(EditTagDto editTagDto) {
        Tag tag = BeanCopyUtils.copyBean(editTagDto, Tag.class);
        tag.setUpdateBy(SecurityUtils.getUserId());
        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTagById(Long tagId) {
        //查询标签
        Tag tag = getById(tagId);
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult listAllTag() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId, Tag::getName);//只查询id name字段
        List<Tag> list = list(queryWrapper);
        List<TagVo> tagVoList = BeanCopyUtils.copyBeanList(list, TagVo.class);
        return ResponseResult.okResult(tagVoList);
    }


    private boolean tagNameExist(String tagName) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getName, tagName);
        return count(queryWrapper) > 0;
    }
}
