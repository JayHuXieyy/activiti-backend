package com.huafagroup.activiti.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huafagroup.activiti.entity.ArticleTable;
import com.huafagroup.activiti.mapper.ArticleTableMapper;
import com.huafagroup.activiti.service.ArticleTableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * </p>
 *
 * @author 13738
 * @date 2021-04-07 18:16:20
 */

@Service
public class ArticleTableServiceImpl extends ServiceImpl
        <ArticleTableMapper, ArticleTable> implements ArticleTableService {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ArticleTableMapper mapper;

}