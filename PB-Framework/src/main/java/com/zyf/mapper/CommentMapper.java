package com.zyf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyf.pojo.entity.Comment;
import org.apache.ibatis.annotations.Mapper;


/**
 * 评论表(Comment)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-01 16:44:17
 */
@Mapper()
public interface CommentMapper extends BaseMapper<Comment> {

}
