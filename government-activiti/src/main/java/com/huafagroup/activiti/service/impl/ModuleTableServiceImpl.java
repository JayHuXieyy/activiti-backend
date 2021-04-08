package com.huafagroup.activiti.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huafagroup.activiti.domain.dto.ModuleTableDto;
import com.huafagroup.activiti.entity.ModuleTable;
import com.huafagroup.activiti.entity.OrganizationTable;
import com.huafagroup.activiti.mapper.ModuleTableMapper;
import com.huafagroup.activiti.service.ModuleTableService;
import com.huafagroup.common.utils.QueryDto;
import com.huafagroup.common.utils.SearchQueryEnum;
import com.huafagroup.system.mapper.SysDeptMapper;
import com.huafagroup.system.service.impl.SysDeptServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private SysDeptServiceImpl sysDeptService;

    @Override
    public List<ModuleTableDto> findPageList(QueryDto queryDto) throws ParseException {
        LambdaQueryWrapper<ModuleTable> queryWrapper= Wrappers.lambdaQuery();
        // 名称查询
        if (queryDto.getSearchValue().get(SearchQueryEnum.TITLE.getValue()) != null) {
            queryWrapper.like(ModuleTable::getTitle,queryDto.getSearchValue().get(SearchQueryEnum.TITLE.getValue()));
        }
        // 页面类型查询
        if (queryDto.getSearchValue().get(SearchQueryEnum.PAGE_TYPE.getValue()) != null) {
            queryWrapper.eq(ModuleTable::getPageType,queryDto.getSearchValue().get(SearchQueryEnum.PAGE_TYPE.getValue()));
        }
        queryWrapper.orderByAsc(ModuleTable::getSort);
        List<ModuleTable> moduleTableList=mapper.selectList(queryWrapper);
        List<ModuleTableDto> moduleTableDtos=new ArrayList<>();

        for (ModuleTable item:moduleTableList){
            ModuleTableDto moduleTableDto=(ModuleTableDto)item;
            moduleTableDto.setDeptName(sysDeptService.getById(item.getDeptId()).getDeptName());
            moduleTableDtos.add(moduleTableDto);
        }

        return moduleTableDtos;
    }


}