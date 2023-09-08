package com.zyf.pojo.dto.list;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户列表dto")
public class UserListDto {
    private String userName;

    private String status;

    private String phonenumber;
}
