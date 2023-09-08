package com.zyf.controller;

import com.zyf.constants.SystemConstants;
import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.dto.add.AddCommentDto;
import com.zyf.pojo.entity.Comment;
import com.zyf.service.CommentService;
import com.zyf.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论接口")
public class CommentController {
    @Resource
    private CommentService commentService;

    @GetMapping("/commentList")
    @ApiOperation(value = "获取评论", notes = "分页获取评论列表")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "pageNum", value = "页码"),
                    @ApiImplicitParam(name = "pageSize", value = "每页大小"),
                    @ApiImplicitParam(name = "articleId", value = "文章id")
            }
    )
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize) {
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT, articleId, pageNum, pageSize);
    }


    @PostMapping()
    @ApiOperation(value = "添加评论", notes = "添加评论")
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto) {
        Comment comment = BeanCopyUtils.copyBean(addCommentDto, Comment.class);
        return commentService.addComment(comment);
    }

    @GetMapping("/linkCommentList")
    @ApiOperation(value = "获取友链评论", notes = "分页获取友链评论列表")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "pageNum", value = "页码"),
                    @ApiImplicitParam(name = "pageSize", value = "每页大小")
            }
    )
    public ResponseResult linkCommentList(Integer pageNum, Integer pageSize) {
        return commentService.commentList(SystemConstants.LINK_COMMENT, null, pageNum, pageSize);
    }
}
