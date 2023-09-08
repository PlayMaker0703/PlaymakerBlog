package com.zyf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyf.constants.SystemConstants;
import com.zyf.mapper.MenuMapper;
import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.dto.add.AddMenuDto;
import com.zyf.pojo.dto.list.MenuListDto;
import com.zyf.pojo.dto.UpdateMenuDto;
import com.zyf.pojo.entity.Menu;
import com.zyf.pojo.vo.MenuTreeVo;
import com.zyf.pojo.vo.RoleMenuTreeSelectVo;
import com.zyf.service.MenuService;
import com.zyf.utils.BeanCopyUtils;
import com.zyf.utils.SecurityUtils;
import com.zyf.utils.SystemConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-09-04 16:07:55
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Resource
    private MenuMapper menuMapper;

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员返回所有的权限
        if (SecurityUtils.isAdmin()) {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            queryWrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(queryWrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //返回其所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        //判断是否为管理员
        List<Menu> menus = null;
        if (SecurityUtils.isAdmin()) {
            //如果是返回所有符合要求的menu
            menus = menuMapper.selectAllRouterMenu();

        } else {
            //否则查询当前用户所具有的menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        //构建menu tree 子父的层级关系
        //先找出第一层的菜单 再根据其对应的id 得到其子菜单 （父菜单的id即为子菜单的parentId）
        List<Menu> menuTree = builderMenuTree(menus, 0L);
        return menuTree;
    }

    @Override
    public List<Menu> selectMenuList(MenuListDto menuListDto) {
        Menu menu = BeanCopyUtils.copyBean(menuListDto, Menu.class);
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        //menuName模糊查询
        queryWrapper.like(StringUtils.hasText(menu.getMenuName()), Menu::getMenuName, menu.getMenuName());
        queryWrapper.eq(StringUtils.hasText(menu.getStatus()), Menu::getStatus, menu.getStatus());
        //排序 parent_id和order_num
        queryWrapper.orderByAsc(Menu::getParentId, Menu::getOrderNum);
        List<Menu> menus = list(queryWrapper);
        return menus;
    }

    @Override
    public ResponseResult addMenu(AddMenuDto addMenuDto) {
        Menu menu = BeanCopyUtils.copyBean(addMenuDto, Menu.class);
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getInfo(Long id) {
        Menu menu = getById(id);
        return ResponseResult.okResult(menu);
    }

    @Override
    public ResponseResult editMenu(UpdateMenuDto updateMenuDto) {
        Menu menu = BeanCopyUtils.copyBean(updateMenuDto, Menu.class);
        if (menu.getId().equals(menu.getParentId())) {
            return ResponseResult.errorResult(500, "修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long menuId) {
        if (hasChild(menuId)) {
            return ResponseResult.errorResult(500, "存在子菜单不允许删除");
        }
        removeById(menuId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult treeselect() {
        List<Menu> menus = selectMenuList(new MenuListDto());
        List<MenuTreeVo> options = SystemConverter.buildMenuSelectTree(menus);
        return ResponseResult.okResult(options);
    }

    @Override
    public ResponseResult roleMenuTreeSelect(Long roleId) {
        //查询menu list
        List<Menu> menus = selectMenuList(new MenuListDto());
        //两个表进行查询
        List<Long> checkedKeys = getBaseMapper().selectMenuListByRoleId(roleId);
        List<MenuTreeVo> menuTreeVos = SystemConverter.buildMenuSelectTree(menus);
        //封装vo返回
        RoleMenuTreeSelectVo roleMenuTreeSelectVo = new RoleMenuTreeSelectVo(checkedKeys, menuTreeVos);
        return ResponseResult.okResult(roleMenuTreeSelectVo);
    }

    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }


    /**
     * 传入一个menu 得到子菜单的集合
     *
     * @param menu
     * @param menus
     */

    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(getChildren(m, menus)))//递归调用 子菜单还有子菜单
                .collect(Collectors.toList());
        return childrenList;
    }


    private boolean hasChild(Long menuId) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId, menuId);
        return count(queryWrapper) != 0;
    }

}
