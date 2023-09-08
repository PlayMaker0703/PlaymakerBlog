package com.zyf.pojo.dto.add;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddLinkDto {
    private Long id;

    private String name;

    private String logo;

    private String description;
    //网站地址
    private String address;

    private String status;

}
