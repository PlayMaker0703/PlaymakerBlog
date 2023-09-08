package com.zyf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyf.constants.SystemConstants;
import com.zyf.mapper.LinkMapper;
import com.zyf.pojo.ResponseResult;
import com.zyf.pojo.dto.add.AddLinkDto;
import com.zyf.pojo.dto.change.ChangeLinkStatusDto;
import com.zyf.pojo.dto.edit.EditLinkDto;
import com.zyf.pojo.dto.list.LinkListDto;
import com.zyf.pojo.entity.Link;
import com.zyf.pojo.vo.LinkVo;
import com.zyf.pojo.vo.PageVo;
import com.zyf.service.LinkService;
import com.zyf.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-08-30 19:12:39
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult gatAllLink() {
        //查询所有审核通过的
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = list(queryWrapper);
        //封装返回
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult selectLinkPage(LinkListDto linkListDto, Integer pageNum, Integer pageSize) {
        Link link = BeanCopyUtils.copyBean(linkListDto, Link.class);

        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.hasText(link.getName()), Link::getName, link.getName());
        queryWrapper.eq(Objects.nonNull(link.getStatus()), Link::getStatus, link.getStatus());

        Page<Link> linkPage = new Page<>();
        linkPage.setCurrent(pageNum);
        linkPage.setSize(pageSize);
        page(linkPage, queryWrapper);

        List<Link> records = linkPage.getRecords();
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(records, LinkVo.class);
        PageVo pageVo = new PageVo(linkVos, linkPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addLink(AddLinkDto addLinkDto) {
        Link link = BeanCopyUtils.copyBean(addLinkDto, Link.class);
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getInfo(Long linkId) {
        getById(linkId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult editLink(EditLinkDto editLinkDto) {
        Link link = BeanCopyUtils.copyBean(editLinkDto, Link.class);
        updateById(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteLink(List<Long> linkIds) {
        removeByIds(linkIds);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeLinkStatus(ChangeLinkStatusDto changeLinkStatusDto) {
        Link link = BeanCopyUtils.copyBean(changeLinkStatusDto, Link.class);
        updateById(link);
        return ResponseResult.okResult();
    }
}
