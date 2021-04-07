package com.huafagroup.activiti.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huafagroup.activiti.entity.OrganizationTable;
import com.huafagroup.activiti.mapper.OrganizationTableMapper;
import com.huafagroup.activiti.service.OrganizationTableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * </p>
 *
 * @author 13738
 * @date 2021-04-07 18:17:08
 */

@Service
public class OrganizationTableServiceImpl extends ServiceImpl
        <OrganizationTableMapper, OrganizationTable> implements OrganizationTableService {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private OrganizationTableMapper mapper;

}