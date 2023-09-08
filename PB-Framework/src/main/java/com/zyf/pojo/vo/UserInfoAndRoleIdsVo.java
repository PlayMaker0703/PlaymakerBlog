package com.zyf.pojo.vo;

import com.zyf.pojo.entity.Role;
import com.zyf.pojo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoAndRoleIdsVo {

    private User user;

    private List<Role> roles;

    private List<Long> roleIds;


}
