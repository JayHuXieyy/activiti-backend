package com.huafagroup.activiti.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huafagroup.activiti.entity.CatalogTable;
import com.huafagroup.activiti.mapper.CatalogTableMapper;
import com.huafagroup.activiti.service.CatalogTableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * </p>
 *
 * @author 13738
 * @date 2021-04-07 18:16:44
 */

@Service
public class CatalogTableServiceImpl extends ServiceImpl
        <CatalogTableMapper, CatalogTable> implements CatalogTableService {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CatalogTableMapper mapper;

}