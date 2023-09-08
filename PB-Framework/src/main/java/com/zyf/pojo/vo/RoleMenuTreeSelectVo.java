package com.zyf.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuTreeSelectVo {
    /**
     * 角色所关联的菜单权限id列表
     */
    private List<Long> checkedKeys;
    /**
     * 菜单树
     */
    private List<MenuTreeVo> menus;

}
