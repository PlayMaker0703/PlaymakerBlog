package com.zyf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.dto.add.AddLinkDto;
import com.zyf.pojo.dto.change.ChangeLinkStatusDto;
import com.zyf.pojo.dto.edit.EditLinkDto;
import com.zyf.pojo.dto.list.LinkListDto;
import com.zyf.pojo.entity.Link;

import java.util.List;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-08-30 19:12:39
 */
public interface LinkService extends IService<Link> {

    ResponseResult gatAllLink();

    ResponseResult selectLinkPage(LinkListDto linkListDto, Integer pageNum, Integer pageSize);

    ResponseResult addLink(AddLinkDto addLinkDto);

    ResponseResult getInfo(Long linkId);

    ResponseResult editLink(EditLinkDto editLinkDto);

    ResponseResult deleteLink(List<Long> linkIds);

    ResponseResult changeLinkStatus(ChangeLinkStatusDto changeLinkStatusDto);
}

