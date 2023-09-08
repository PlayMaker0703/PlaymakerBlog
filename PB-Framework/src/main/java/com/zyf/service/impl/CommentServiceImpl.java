package com.zyf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyf.constants.SystemConstants;
import com.zyf.enums.AppHttpCodeEnum;
import com.zyf.exception.SystemException;
import com.zyf.mapper.CommentMapper;
import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.entity.Comment;
import com.zyf.pojo.vo.CommentVo;
import com.zyf.pojo.vo.PageVo;
import com.zyf.service.CommentService;
import com.zyf.service.UserService;
import com.zyf.utils.BeanCopyUtils;
import com.zyf.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-09-01 16:44:17
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Resource
    private UserService userService;

    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        //对应文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();

        //对articleId进行判断
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType), Comment::getArticleId, articleId);


        //根评论rootId为-1
        queryWrapper.eq(Comment::getRootId, SystemConstants.ROOT_COMMENT);

        //评论类型
        queryWrapper.eq(Comment::getType,commentType);

        //分页查询
        Page<Comment> commentPage = new Page<>(pageNum, pageSize);
        page(commentPage, queryWrapper);

        List<CommentVo> commentVoList = toCommentVoList(commentPage.getRecords());

        //查询所有根评论对应的子评论集合，并且赋值给对应的属性
        commentVoList.forEach(commentVo -> {
            List<CommentVo> children = getChildren(commentVo.getId());
            commentVo.setChildren(children);
        });

        //查询所有根评论对应的子评论集合 并且赋值给对应的属性
        return ResponseResult.okResult(new PageVo(commentVoList, commentPage.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        comment.setCreateBy(SecurityUtils.getUserId());
        //评论内容不能为空
        if (!StringUtils.hasText(comment.getContent())) {
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }

//    private List<CommentVo> toCommentVoList(List<Comment> list) {
//        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
//        //遍历vo集合
//        for (CommentVo commentVo : commentVos) {
//            //通过creatBy查询用户的昵称并赋值
//            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
//            commentVo.setUsername(nickName);
//            //通过toCommentUserId查询用户的昵称并赋值
//            //如果toCommentUserId不为-1才进行查询
//            if (commentVo.getToCommentUserId() != -1) {
//                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
//                commentVo.setToCommentUserName(toCommentUserName);
//            }
//        }
//        return commentVos;
//    }

    /**
     * @param id 根据根评论的id 查询所对应的子评论的集合
     * @return
     */
    private List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId, id);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);
        List<CommentVo> commentVos = toCommentVoList(comments);
        return commentVos;
    }

    private List<CommentVo> toCommentVoList(List<Comment> list) {
        return list.stream()
                .map(comment -> {
                    CommentVo commentVo = BeanCopyUtils.copyBean(comment, CommentVo.class);
                    //通过creatBy查询用户的昵称并赋值
                    String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
                    commentVo.setUsername(nickName);
                    //通过toCommentUserId查询用户的昵称并赋值
                    //如果toCommentUserId不为-1才进行查询
                    if (commentVo.getToCommentUserId() != -1) {
                        String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                        commentVo.setToCommentUserName(toCommentUserName);
                    }
                    return commentVo;
                })
                .collect(Collectors.toList());
    }

}
