package com.zyf.controller;

import com.zyf.annotation.SystemLog;
import com.zyf.exception.SystemException;
import com.zyf.pojo.ResponseResult;
import com.zyf.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
public class UploadController {
    @Resource
    private UploadService uploadService;

    @PostMapping("/upload")
    public ResponseResult uploadImag(@RequestParam("img") MultipartFile img) {
        try {
            return uploadService.uploadImag(img);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传失败");
        }

    }
}
