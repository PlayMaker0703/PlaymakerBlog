package com.zyf.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVo {

    private long id;

    private String name;

    //描述
    private String description;

    private String status;
}
