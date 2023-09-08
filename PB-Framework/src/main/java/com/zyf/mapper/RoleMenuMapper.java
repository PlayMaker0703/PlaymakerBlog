package com.zyf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyf.pojo.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;


/**
 * 角色和菜单关联表(RoleMenu)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-07 15:31:49
 */
@Mapper()
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

}
