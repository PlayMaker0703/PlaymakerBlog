package com.zyf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyf.pojo.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-04 16:14:39
 */
@Mapper()
public interface RoleMapper extends BaseMapper<Role> {
    @Select("SELECT r.role_key " +
            "FROM sys_user_role ur " +
            "LEFT JOIN sys_role r ON ur.role_id = r.id " +
            "WHERE ur.user_id = #{userId} AND " +
            "r.status = 0 AND " +
            "r.del_flag = 0")
    List<String> selectRoleKeyByUserId(@Param("userId") Long id);

    @Select("SELECT r.id " +
            "FROM sys_role r " +
            "LEFT JOIN sys_user_role ur ON ur.role_id = r.id " +
            "WHERE ur.user_id = #{userId}")
    List<Long> selectRoleIdByUserId(@Param("userId") Long userId);


}
