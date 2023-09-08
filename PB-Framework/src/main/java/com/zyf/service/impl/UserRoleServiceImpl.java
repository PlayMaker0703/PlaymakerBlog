package com.zyf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyf.mapper.UserRoleMapper;
import com.zyf.pojo.entity.UserRole;
import com.zyf.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2023-09-07 18:43:02
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
