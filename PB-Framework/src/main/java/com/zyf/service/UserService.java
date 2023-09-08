package com.zyf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.dto.add.AddUserDto;
import com.zyf.pojo.dto.change.ChangeUserStatusDto;
import com.zyf.pojo.dto.edit.EditUserDto;
import com.zyf.pojo.dto.list.UserListDto;
import com.zyf.pojo.entity.User;

import java.util.List;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-09-01 17:32:42
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult selectUserPage(UserListDto userListDto, Integer pageNum, Integer pageSize);

    ResponseResult addUser(AddUserDto addUserDto);

    ResponseResult removeUser(List<Long> userIds);

    ResponseResult getUserInfoAndRoleIds(Long userId);

    ResponseResult editUser(EditUserDto editUserDto);

    ResponseResult changeStatus(ChangeUserStatusDto changeUserStatusDto);
}

