package com.zyf.controller;

import com.zyf.pojo.ResponseResult;
import com.zyf.service.LinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/link")
@Api(tags = "友链接口")
public class LinkController {
    @Resource
    private LinkService linkService;

    @GetMapping("/getAllLink")
    @ApiOperation(value = "友链", notes = "获取友链列表")
    public ResponseResult getAllLink() {
        return linkService.gatAllLink();
    }
}
