package com.zyf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyf.pojo.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


/**
 * 标签(Tag)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-04 14:04:42
 */
@Mapper()
public interface TagMapper extends BaseMapper<Tag> {

}
