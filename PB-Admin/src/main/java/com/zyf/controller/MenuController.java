package com.zyf.controller;

import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.dto.add.AddMenuDto;
import com.zyf.pojo.dto.list.MenuListDto;
import com.zyf.pojo.dto.UpdateMenuDto;
import com.zyf.pojo.entity.Menu;
import com.zyf.pojo.vo.MenuVo;
import com.zyf.service.MenuService;
import com.zyf.utils.BeanCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Resource
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseResult list(MenuListDto menuListDto) {
        List<Menu> menus = menuService.selectMenuList(menuListDto);
        //封装vo返回
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }

    @PostMapping()
    public ResponseResult addMenu(@RequestBody AddMenuDto addMenuDto) {
        return menuService.addMenu(addMenuDto);
    }

    /**
     * 根据编号获取菜单详细信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{menuId}")
    public ResponseResult getInfo(@PathVariable("menuId") Long id) {
        return menuService.getInfo(id);
    }

    @PutMapping
    public ResponseResult editMenu(@RequestBody UpdateMenuDto updateMenuDto) {
        return menuService.editMenu(updateMenuDto);
    }

    @DeleteMapping("/{menuId}")
    public ResponseResult deleteMenu(@PathVariable("menuId") Long menuId) {
        return menuService.deleteMenu(menuId);
    }

    /**
     * 获取菜单树
     *
     * @return
     */
    @GetMapping("/treeselect")
    public ResponseResult treeselect() {
        return menuService.treeselect();
    }

    /**
     * 加载对应角色菜单列表树接口
     */

    @GetMapping("roleMenuTreeselect/{roleId}")
    public ResponseResult roleMenuTreeSelect(@PathVariable("roleId") Long roleId) {
        return menuService.roleMenuTreeSelect(roleId);
    }

}
