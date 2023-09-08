package com.zyf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyf.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-31 10:10:23
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
