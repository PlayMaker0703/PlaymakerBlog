package com.zyf.controller;

import com.zyf.annotation.SystemLog;
import com.zyf.enums.AppHttpCodeEnum;
import com.zyf.exception.SystemException;
import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.dto.LoginUserDto;
import com.zyf.pojo.entity.User;
import com.zyf.service.BlogLoginService;
import com.zyf.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.util.StringUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "登录接口")
public class BlogLoginController {
    @Resource
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    @SystemLog(businessName = "登录")
    @ApiOperation(value = "登录", notes = "用户登录")
    public ResponseResult login(@RequestBody LoginUserDto loginUserDto) {
        User user = BeanCopyUtils.copyBean(loginUserDto, User.class);
        if (!StringUtils.hasText(user.getUserName())) {
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }

        return blogLoginService.login(user);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "登出", notes = "用户登出")
    public ResponseResult logout() {
        return blogLoginService.logout();
    }
}
