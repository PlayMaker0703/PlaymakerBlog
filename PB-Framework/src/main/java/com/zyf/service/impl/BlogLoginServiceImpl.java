package com.zyf.service.impl;

import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.entity.LoginUser;
import com.zyf.pojo.entity.User;
import com.zyf.pojo.vo.BlogUserLoginVo;
import com.zyf.pojo.vo.UserInfoVo;
import com.zyf.service.BlogLoginService;
import com.zyf.utils.BeanCopyUtils;
import com.zyf.utils.JwtUtil;
import com.zyf.utils.RedisCache;
import com.zyf.utils.SecurityUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {
    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否通过
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        //用户信息存入redis
        String jwt = JwtUtil.createJWT(userId);
        redisCache.setCacheObject("bloglogin:" + userId, loginUser);
        //token userInfo封装成 返回
        //把user转换为userInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo userLoginVo = new BlogUserLoginVo(jwt, userInfoVo);
        return ResponseResult.okResult(userLoginVo);
    }

    @Override
    public ResponseResult logout() {
        Long userId = SecurityUtils.getUserId();
        //删除redis中的用户数据
        redisCache.deleteObject("bloglogin:" + userId);
        return ResponseResult.okResult();
    }
}
