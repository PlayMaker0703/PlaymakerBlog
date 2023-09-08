package com.zyf.controller;

import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.dto.add.AddRoleDto;
import com.zyf.pojo.dto.change.ChangeRoleStatusDto;
import com.zyf.pojo.dto.edit.EditRoleDto;
import com.zyf.pojo.dto.list.RoleListDto;
import com.zyf.pojo.entity.Role;
import com.zyf.service.RoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Resource
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult list(RoleListDto roleListDto, Integer pageNum, Integer pageSize) {
        return roleService.selectRolePage(roleListDto, pageNum, pageSize);
    }


    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeRoleStatusDto changeRoleStatusDto) {
        return roleService.changeStatus(changeRoleStatusDto);
    }

    @PostMapping
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto) {
        roleService.insertRole(addRoleDto);
        return ResponseResult.okResult();
    }

    @GetMapping("/{roleId}")
    public ResponseResult getInfo(@PathVariable("roleId") Long id) {
        return roleService.getInfo(id);
    }

    @PutMapping
    public ResponseResult editRole(@RequestBody EditRoleDto editRoleDto) {
        return roleService.editRole(editRoleDto);
    }

    @DeleteMapping("/{roleIds}")
    public ResponseResult deleteRole(@PathVariable("roleIds") List<Long> roleIds) {
        return roleService.deleteRole(roleIds);
    }


    @GetMapping("/listAllRole")
    public ResponseResult listAllRole() {
        List<Role> roles = roleService.selectRoleAll();
        return ResponseResult.okResult(roles);
    }

}
