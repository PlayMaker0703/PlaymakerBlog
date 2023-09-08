package com.zyf.pojo.dto.list;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "标签列表dto")
public class TagListDto {


    //标签名
    private String name;

    //备注
    private String remark;

}
