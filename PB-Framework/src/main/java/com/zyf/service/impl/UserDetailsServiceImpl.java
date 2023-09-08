package com.zyf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyf.constants.SystemConstants;
import com.zyf.mapper.MenuMapper;
import com.zyf.mapper.UserMapper;
import com.zyf.pojo.entity.LoginUser;
import com.zyf.pojo.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    UserMapper userMapper;

    @Resource
    MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(queryWrapper);
        //判断是否查到用户 如果没查到抛出异常
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户不存在");
        }
        //查到用户 返回用户信息
        // 查询权限信息封装
        // 如果是后台用户才需要查询权限信息 进行封装
        if (user.getType().equals(SystemConstants.ADMAIN)) {
            List<String> list = menuMapper.selectPermsByUserId(user.getId());
            return new LoginUser(user, list);
        }
        //非管理员
        return new LoginUser(user, null);
    }
}
