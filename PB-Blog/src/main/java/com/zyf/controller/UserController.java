package com.zyf.controller;

import com.zyf.annotation.SystemLog;
import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.dto.RegisterUserDto;
import com.zyf.pojo.dto.UpdateUserInfoDto;
import com.zyf.pojo.entity.User;
import com.zyf.service.UserService;
import com.zyf.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
@Api(tags = "用户接口")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/userInfo")
    @ApiOperation(value = "获取用户信息")
    public ResponseResult userInfo() {
        return userService.userInfo();
    }


    @PutMapping("/userInfo")
    @SystemLog(businessName = "更新用户信息")
    @ApiOperation(value = "更新用户信息")
    public ResponseResult updateUserInfo(@RequestBody UpdateUserInfoDto updateUserInfoDto) {
        User user = BeanCopyUtils.copyBean(updateUserInfoDto, User.class);
        return userService.updateUserInfo(user);
    }

    @PostMapping("/register")
    @SystemLog(businessName = "注册")
    @ApiOperation(value = "用户注册")
    public ResponseResult register(@RequestBody RegisterUserDto registerUserDto) {
        User user = BeanCopyUtils.copyBean(registerUserDto, User.class);
        return userService.register(user);
    }
}
