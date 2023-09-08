package com.zyf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyf.pojo.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-04 16:07:55
 */
@Mapper()
public interface MenuMapper extends BaseMapper<Menu> {


    @Select("SELECT DISTINCT m.perms " +
            "FROM sys_user_role ur " +
            "LEFT JOIN sys_role_menu rm ON ur.role_id = rm.role_id " +
            "LEFT JOIN sys_menu m ON m.id = rm.menu_id " +
            "WHERE ur.user_id = #{userId} AND " +
            "m.menu_type IN ('C','F') AND " +
            "m.status = 0 AND " +
            "m.del_flag = 0")
        //联查权限
    List<String> selectPermsByUserId(@Param("userId") Long userId);


    @Select("SELECT DISTINCT m.id, m.parent_id, m.menu_name, m.path, m.component, m.visible, m.status, IFNULL(m.perms,'') AS perms, m.is_frame,  m.menu_type, m.icon, m.order_num, m.create_time " +
            "FROM sys_menu m " +
            "WHERE m.menu_type IN ('C','M') AND " +
            "m.status = 0 AND " +
            "m.del_flag = 0 " +
            "ORDER BY m.parent_id, m.order_num")
    List<Menu> selectAllRouterMenu();

    @Select("SELECT DISTINCT m.id, m.parent_id, m.menu_name, m.path, m.component, m.visible, m.status, IFNULL(m.perms,'') AS perms, m.is_frame,  m.menu_type, m.icon, m.order_num, m.create_time " +
            "FROM sys_user_role ur " +
            "LEFT JOIN sys_role_menu rm ON ur.role_id = rm.role_id " +
            "LEFT JOIN sys_menu m ON m.id = rm.menu_id " +
            "WHERE ur.user_id = #{userId} AND " +
            "m.menu_type IN ('C','M') AND " +
            "m.status = 0 AND " +
            "m.del_flag = 0 " +
            "ORDER BY m.parent_id, m.order_num")
    List<Menu> selectRouterMenuTreeByUserId(@Param("userId") Long userId);


    @Select("SELECT m.id, m.parent_id, m.order_num " +
            "FROM sys_menu m " +
            "LEFT JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "WHERE rm.role_id = #{roleId} " +
            "ORDER BY m.parent_id, m.order_num")
    List<Long> selectMenuListByRoleId(@Param("roleId") Long roleId);

}
