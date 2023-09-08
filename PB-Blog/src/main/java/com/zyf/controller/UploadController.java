package com.zyf.controller;

import com.aliyun.oss.model.MultipartUpload;
import com.zyf.annotation.SystemLog;
import com.zyf.pojo.ResponseResult;
import com.zyf.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@Api(tags = "上传图片接口")
public class UploadController {
    @Resource
    private UploadService uploadService;

    @PostMapping("/upload")
    @SystemLog(businessName = "上传头像")
    @ApiOperation(value = "上传图片")
    @ApiImplicitParam(name = "img", value = "图片对象")
    public ResponseResult uploadImag(MultipartFile img) {
        return uploadService.uploadImag(img);
    }
}
