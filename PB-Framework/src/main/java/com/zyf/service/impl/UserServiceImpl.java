package com.zyf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyf.enums.AppHttpCodeEnum;
import com.zyf.exception.SystemException;
import com.zyf.mapper.UserMapper;
import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.dto.add.AddUserDto;
import com.zyf.pojo.dto.change.ChangeUserStatusDto;
import com.zyf.pojo.dto.edit.EditUserDto;
import com.zyf.pojo.dto.list.UserListDto;
import com.zyf.pojo.entity.Role;
import com.zyf.pojo.entity.User;
import com.zyf.pojo.entity.UserRole;
import com.zyf.pojo.vo.PageVo;
import com.zyf.pojo.vo.UserInfoAndRoleIdsVo;
import com.zyf.pojo.vo.UserInfoVo;
import com.zyf.pojo.vo.UserVo;
import com.zyf.service.RoleService;
import com.zyf.service.UserRoleService;
import com.zyf.service.UserService;
import com.zyf.utils.BeanCopyUtils;
import com.zyf.utils.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-09-01 17:32:42
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private RoleService roleService;

    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在进行判断
        if (userNameExist(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (nickNameExist(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (emailExist(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //对密码进行加密
        String encoderPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encoderPassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult selectUserPage(UserListDto userListDto, Integer pageNum, Integer pageSize) {
        User user = BeanCopyUtils.copyBean(userListDto, User.class);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(StringUtils.hasText(user.getUserName()), User::getUserName, user.getUserName());
        queryWrapper.eq(StringUtils.hasText(user.getStatus()), User::getStatus, user.getStatus());
        queryWrapper.eq(StringUtils.hasText(user.getPhonenumber()), User::getPhonenumber, user.getPhonenumber());

        Page<User> userPage = new Page<>();
        userPage.setCurrent(pageNum);
        userPage.setSize(pageSize);
        page(userPage, queryWrapper);

        //转换成VO
        List<User> users = userPage.getRecords();
        List<UserVo> userVoList = users.stream()
                .map(u -> BeanCopyUtils.copyBean(u, UserVo.class))
                .collect(Collectors.toList());
        PageVo pageVo = new PageVo(userVoList, userPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addUser(AddUserDto addUserDto) {
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);

        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if (!checkUserNameUnique(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (!checkPhoneUnique(user)) {
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        if (!checkEmailUnique(user)) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }

        //密码加密处理
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);

        if (user.getRoleIds() != null && user.getRoleIds().length > 0) {
            insertUserRole(user);
        }

        return ResponseResult.okResult();
    }

    /**
     * 删除
     *
     * @param userIds
     * @return
     */

    @Override
    public ResponseResult removeUser(List<Long> userIds) {
        if (userIds.contains(SecurityUtils.getUserId())) {
            return ResponseResult.errorResult(500, "不能删除当前你正在使用的用户");
        }
        removeByIds(userIds);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserInfoAndRoleIds(Long userId) {
        List<Role> roles = roleService.selectRoleAll();
        User user = getById(userId);
        //当前用户所具有的角色id列表
        List<Long> roleIds = roleService.selectRoleIdByUserId(userId);
        //封装Vo
        UserInfoAndRoleIdsVo userInfoAndRoleIdsVo = new UserInfoAndRoleIdsVo(user, roles, roleIds);
        return ResponseResult.okResult(userInfoAndRoleIdsVo);
    }

    @Override
    public ResponseResult editUser(EditUserDto editUserDto) {
        User user = BeanCopyUtils.copyBean(editUserDto, User.class);
        // 删除用户与角色关联
        LambdaQueryWrapper<UserRole> userRoleUpdateWrapper = new LambdaQueryWrapper<>();
        userRoleUpdateWrapper.eq(UserRole::getUserId, user.getId());
        userRoleService.remove(userRoleUpdateWrapper);

        // 新增用户与角色管理
        insertUserRole(user);
        // 更新用户信息
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeStatus(ChangeUserStatusDto changeUserStatusDto) {
        User user = new User();
        user.setId(changeUserStatusDto.getUserId());
        user.setStatus(changeUserStatusDto.getStatus());
        updateById(user);
        return ResponseResult.okResult();
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userName);
        return count(queryWrapper) > 0;
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName, nickName);
        return count(queryWrapper) > 0;
    }

    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        return count(queryWrapper) > 0;
    }

    public boolean checkUserNameUnique(String userName) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getUserName, userName)) == 0;
    }


    public boolean checkPhoneUnique(User user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getPhonenumber, user.getPhonenumber())) == 0;
    }


    public boolean checkEmailUnique(User user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getEmail, user.getEmail())) == 0;
    }

    //得到user role的关系集合
    private void insertUserRole(User user) {
        List<UserRole> sysUserRoles = Arrays.stream(user.getRoleIds())
                .map(roleId -> new UserRole(user.getId(), roleId)).collect(Collectors.toList());
        userRoleService.saveBatch(sysUserRoles);
    }


}
