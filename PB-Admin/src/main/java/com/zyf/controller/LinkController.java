package com.zyf.controller;

import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.dto.add.AddLinkDto;
import com.zyf.pojo.dto.change.ChangeLinkStatusDto;
import com.zyf.pojo.dto.edit.EditLinkDto;
import com.zyf.pojo.dto.list.LinkListDto;
import com.zyf.service.LinkService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Resource
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult list(LinkListDto linkListDto,Integer pageNum,Integer pageSize){
        return linkService.selectLinkPage(linkListDto,pageNum,pageSize);
    }

    @PostMapping
    public ResponseResult addLink(@RequestBody AddLinkDto addLinkDto){
        return linkService.addLink(addLinkDto);
    }

    @GetMapping("/{linkId}")
    public ResponseResult getInfo(@PathVariable("linkId")Long linkId){
        return linkService.getInfo(linkId);
    }

    @PutMapping
    public ResponseResult editLink(@RequestBody EditLinkDto editLinkDto){
        return linkService.editLink(editLinkDto);
    }

    @DeleteMapping("/{linkIds}")
    public ResponseResult deleteLink(@PathVariable("linkIds")List<Long> linkIds){
        return linkService.deleteLink(linkIds);
    }

    @PutMapping("/changeLinkStatus")
    public ResponseResult changeLinkStatus(@RequestBody ChangeLinkStatusDto changeLinkStatusDto){
        return linkService.changeLinkStatus(changeLinkStatusDto);
    }
}
