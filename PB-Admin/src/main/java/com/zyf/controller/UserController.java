package com.zyf.controller;

import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.dto.add.AddUserDto;
import com.zyf.pojo.dto.change.ChangeUserStatusDto;
import com.zyf.pojo.dto.edit.EditUserDto;
import com.zyf.pojo.dto.list.UserListDto;
import com.zyf.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/user")
@Api(tags = "管理员接口")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/list")
    public ResponseResult list(UserListDto userListDto, Integer pageNum, Integer pageSize) {
        return userService.selectUserPage(userListDto, pageNum, pageSize);
    }

    @PostMapping()
    public ResponseResult addUser(@RequestBody AddUserDto addUserDto) {
        return userService.addUser(addUserDto);
    }

    @DeleteMapping("/{userIds}")
    public ResponseResult removeUser(@PathVariable("userIds") List<Long> userIds) {
        return userService.removeUser(userIds);
    }

    @GetMapping("/{userId}")
    public ResponseResult getUserInfoAndRoleIds(@PathVariable(value = "userId") Long userId) {
        return userService.getUserInfoAndRoleIds(userId);
    }

    @PutMapping
    public ResponseResult editUser(@RequestBody EditUserDto editUserDto) {
        return userService.editUser(editUserDto);

    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeUserStatusDto changeUserStatusDto){
        return userService.changeStatus(changeUserStatusDto);
    }

}
