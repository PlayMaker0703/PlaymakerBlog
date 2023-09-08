package com.zyf.controller;

import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.dto.add.AddTagDto;
import com.zyf.pojo.dto.edit.EditTagDto;
import com.zyf.pojo.dto.list.TagListDto;
import com.zyf.pojo.entity.Tag;
import com.zyf.pojo.vo.PageVo;
import com.zyf.service.TagService;
import com.zyf.utils.BeanCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Resource
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        return tagService.pageTagList(pageNum, pageSize, tagListDto);
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody AddTagDto addTagDto) {
        Tag tag = BeanCopyUtils.copyBean(addTagDto, Tag.class);
        return tagService.addTag(tag);
    }

    @DeleteMapping("/{tagIds}")
    public ResponseResult deleteTag(@PathVariable("tagIds") List<Long> tagIds) {
        return tagService.deleteTag(tagIds);
    }

    @GetMapping("/{tagId}")
    public ResponseResult getTag(@PathVariable("tagId") Long tagId) {
        return tagService.getTagById(tagId);
    }


    @PutMapping
    public ResponseResult editTag(@RequestBody EditTagDto editTagDto) {
        return tagService.editTag(editTagDto);
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag() {
        return tagService.listAllTag();
    }


}
