package com.zyf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.dto.add.AddMenuDto;
import com.zyf.pojo.dto.list.MenuListDto;
import com.zyf.pojo.dto.UpdateMenuDto;
import com.zyf.pojo.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-09-04 16:07:55
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    List<Menu> selectMenuList(MenuListDto menuListDto);

    ResponseResult addMenu(AddMenuDto addMenuDto);

    ResponseResult getInfo(Long id);

    ResponseResult editMenu(UpdateMenuDto updateMenuDto);

    ResponseResult deleteMenu(Long menuId);

    ResponseResult treeselect();

    ResponseResult roleMenuTreeSelect(Long roleId);
}

