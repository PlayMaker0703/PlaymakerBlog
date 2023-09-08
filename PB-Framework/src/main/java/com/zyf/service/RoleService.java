package com.zyf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.dto.add.AddRoleDto;
import com.zyf.pojo.dto.change.ChangeRoleStatusDto;
import com.zyf.pojo.dto.edit.EditRoleDto;
import com.zyf.pojo.dto.list.RoleListDto;
import com.zyf.pojo.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-09-04 16:14:39
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult selectRolePage(RoleListDto roleListDto, Integer pageNum, Integer pageSize);

    ResponseResult changeStatus(ChangeRoleStatusDto changeRoleStatusDto);


    void insertRole(AddRoleDto addRoleDto);

    ResponseResult getInfo(Long id);

    ResponseResult editRole(EditRoleDto editRoleDto);

    ResponseResult deleteRole(List<Long> roleIds);

    List<Role> selectRoleAll();

    List<Long> selectRoleIdByUserId(Long userId);
}

