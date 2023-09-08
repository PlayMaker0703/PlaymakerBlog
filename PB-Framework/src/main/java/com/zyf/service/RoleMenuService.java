package com.zyf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyf.pojo.entity.RoleMenu;


/**
 * 角色和菜单关联表(RoleMenu)表服务接口
 *
 * @author makejava
 * @since 2023-09-07 15:31:49
 */
public interface RoleMenuService extends IService<RoleMenu> {
    void deleteRoleMenuByRoleId(Long id);
}

