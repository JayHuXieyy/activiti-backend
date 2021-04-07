package com.huafagroup.activiti.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huafagroup.activiti.entity.ModuleTable;
import com.huafagroup.activiti.mapper.ModuleTableMapper;
import com.huafagroup.activiti.service.ModuleTableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * </p>
 *
 * @author 13738
 * @date 2021-04-07 18:16:58
 */

@Service
public class ModuleTableServiceImpl extends ServiceImpl
        <ModuleTableMapper, ModuleTable> implements ModuleTableService {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ModuleTableMapper mapper;

}