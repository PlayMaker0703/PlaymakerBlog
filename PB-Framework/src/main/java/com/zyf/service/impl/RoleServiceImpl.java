package com.zyf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyf.constants.SystemConstants;
import com.zyf.mapper.RoleMapper;
import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.dto.add.AddRoleDto;
import com.zyf.pojo.dto.change.ChangeRoleStatusDto;
import com.zyf.pojo.dto.edit.EditRoleDto;
import com.zyf.pojo.dto.list.RoleListDto;
import com.zyf.pojo.entity.Role;
import com.zyf.pojo.entity.RoleMenu;
import com.zyf.pojo.vo.PageVo;
import com.zyf.pojo.vo.RoleVo;
import com.zyf.service.RoleMenuService;
import com.zyf.service.RoleService;
import com.zyf.utils.BeanCopyUtils;
import com.zyf.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-09-04 16:14:39
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Resource
    private RoleMenuService roleMenuService;


    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 如果是返回集合中只需要有admin
        if (SecurityUtils.isAdmin()) {
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //查询用户具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult selectRolePage(RoleListDto roleListDto, Integer pageNum, Integer pageSize) {
        Role role = BeanCopyUtils.copyBean(roleListDto, Role.class);
        //目前没有根据id查询
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        //模糊查询
        queryWrapper.like(StringUtils.hasText(role.getRoleName()), Role::getRoleName, role.getRoleName());
        queryWrapper.eq(StringUtils.hasText(role.getStatus()), Role::getStatus, role.getStatus());
        queryWrapper.orderByAsc(Role::getRoleSort);
        //分页查询
        Page<Role> rolePage = new Page<>();
        rolePage.setCurrent(pageNum);
        rolePage.setSize(pageSize);
        page(rolePage, queryWrapper);

        PageVo pageVo = new PageVo(rolePage.getRecords(), rolePage.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult changeStatus(ChangeRoleStatusDto changeRoleStatusDto) {
        Role role = new Role();
        role.setId(changeRoleStatusDto.getRoleId());
        role.setStatus(changeRoleStatusDto.getStatus());
        updateById(role);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public void insertRole(AddRoleDto addRoleDto) {
        Role role = BeanCopyUtils.copyBean(addRoleDto, Role.class);
        save(role);
        System.out.println(role.getId());
        if (role.getMenuIds() != null && role.getMenuIds().length > 0) {
            insertRoleMenu(role);
        }
    }

    @Override
    public ResponseResult getInfo(Long id) {
        Role role = getById(id);
        RoleVo roleVo = BeanCopyUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    @Override
    public ResponseResult editRole(EditRoleDto editRoleDto) {
        Role role = BeanCopyUtils.copyBean(editRoleDto, Role.class);
        updateById(role);
        //更新后对有关的menu role表中的数据进行删除
        roleMenuService.deleteRoleMenuByRoleId(role.getId());
        insertRoleMenu(role);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRole(List<Long> roleIds) {
        removeByIds(roleIds);
        return ResponseResult.okResult();
    }

    @Override
    public List<Role> selectRoleAll() {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        //查询状态正常的
        queryWrapper.eq(Role::getStatus, SystemConstants.NORMAL);
        return list(queryWrapper);
    }

    @Override
    public List<Long> selectRoleIdByUserId(Long userId) {
        return getBaseMapper().selectRoleIdByUserId(userId);
    }

    /**
     * 存入关联菜单的id
     *
     * @param role
     */
    private void insertRoleMenu(Role role) {
        List<RoleMenu> roleMenuList = Arrays.stream(role.getMenuIds())
                .map(memuId -> new RoleMenu(role.getId(), memuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);
    }
}
