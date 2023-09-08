package com.zyf.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyf.mapper.ArticleTagMapper;
import com.zyf.pojo.entity.ArticleTag;
import com.zyf.service.ArticleTagService;
import org.springframework.stereotype.Service;

@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
