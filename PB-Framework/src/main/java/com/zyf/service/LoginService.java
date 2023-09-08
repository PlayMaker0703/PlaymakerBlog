package com.zyf.service;

import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.entity.User;


public interface LoginService {
     ResponseResult login(User user);
     ResponseResult logout();
}
