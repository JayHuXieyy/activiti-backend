package com.huafagroup.activiti.service.biz.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.huafagroup.activiti.domain.dto.BizTerminalIndexDto;
import com.huafagroup.activiti.entity.ModuleTable;
import com.huafagroup.activiti.service.ModuleTableService;
import com.huafagroup.activiti.service.biz.BizTerminalService;

import com.huafagroup.common.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BizTerminalServiceImpl implements BizTerminalService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ModuleTableService moduleTableService;

    @Override
    public BizTerminalIndexDto index() {
        BizTerminalIndexDto indexDto = new BizTerminalIndexDto();
        LambdaQueryWrapper<ModuleTable> moduleQuery = Wrappers.lambdaQuery();
        //获取党模块
        moduleQuery.eq(ModuleTable::getPageType, Constants.status_0)
                .eq(ModuleTable::getDelFlag, Constants.status_0)
                .orderByAsc(ModuleTable::getSort)
                .last("limit 5");
        List<ModuleTable> partyModule = moduleTableService.list(moduleQuery);
        moduleQuery.clear();
        //获取生产模块
        moduleQuery.eq(ModuleTable::getPageType, Constants.status_1)
                .eq(ModuleTable::getDelFlag, Constants.status_0)
                .orderByAsc(ModuleTable::getSort)
                .last("limit 3");
        List<ModuleTable> productionModule = moduleTableService.list(moduleQuery);
        moduleQuery.clear();
        //获取生活模块
        moduleQuery.eq(ModuleTable::getPageType, Constants.status_2)
                .eq(ModuleTable::getDelFlag, Constants.status_0)
                .orderByAsc(ModuleTable::getSort)
                .last("limit 3");
        List<ModuleTable> lifeModule = moduleTableService.list(moduleQuery);
        moduleQuery.clear();
        indexDto.setPartyModule(partyModule);
        indexDto.setProductionModule(productionModule);
        indexDto.setLifeModule(lifeModule);

        return indexDto;
    }

    @Override
    public List<ModuleTable> moreModule(Integer pageType) {
        LambdaQueryWrapper<ModuleTable> queryWrapper=Wrappers.lambdaQuery();
        queryWrapper.eq(ModuleTable::getPageType,pageType)
                .eq(ModuleTable::getDelFlag,Constants.status_0)
                .orderByAsc(ModuleTable::getSort);
        List<ModuleTable> moduleTables=moduleTableService.list(queryWrapper);
        return moduleTables;
    }
}
