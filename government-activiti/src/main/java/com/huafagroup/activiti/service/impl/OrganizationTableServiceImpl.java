package com.huafagroup.activiti.service.impl;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huafagroup.activiti.domain.dto.OrganizationNotCountryDto;
import com.huafagroup.activiti.entity.OrganizationTable;
import com.huafagroup.activiti.mapper.OrganizationTableMapper;
import com.huafagroup.activiti.service.OrganizationTableService;
import com.huafagroup.common.core.domain.entity.SysUser;
import com.huafagroup.common.exception.ParamException;
import com.huafagroup.common.utils.QueryDto;

import com.huafagroup.common.utils.SearchQueryEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @date 2021-04-07 18:17:08
 */

@Service
public class OrganizationTableServiceImpl extends ServiceImpl
        <OrganizationTableMapper, OrganizationTable> implements OrganizationTableService {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private OrganizationTableMapper mapper;

    @Override
    public List<OrganizationTable> findPageList(QueryDto queryDto) throws ParseException {
        LambdaQueryWrapper<OrganizationTable> queryWrapper= Wrappers.lambdaQuery();
        // 组织名称查询
        if (queryDto.getSearchValue().get(SearchQueryEnum.TITLE.getValue()) != null) {
            queryWrapper.like(OrganizationTable::getTitle,queryDto.getSearchValue().get(SearchQueryEnum.TITLE.getValue()));
        }
        // 状态查询
        if (queryDto.getSearchValue().get(SearchQueryEnum.STATUS.getValue()) != null) {
            queryWrapper.eq(OrganizationTable::getStatus,queryDto.getSearchValue().get(SearchQueryEnum.STATUS.getValue()));
        }
        queryWrapper.orderByAsc(OrganizationTable::getSort);
        List<OrganizationTable> organizationTableList=mapper.selectList(queryWrapper);
        return organizationTableList;
    }

    @Override
    public Boolean delete(String id) {
        LambdaQueryWrapper<OrganizationTable> queryWrapper= Wrappers.lambdaQuery();
        queryWrapper.eq(OrganizationTable::getParentId,id);
        List<OrganizationTable> organizationTables=mapper.selectList(queryWrapper);
        if(organizationTables.size()>0){
            throw new ParamException("存在子组织，无法删除！");
        }
        return this.removeById(id);
    }

    @Override
    public List<OrganizationNotCountryDto> getlistNotcounty() {
        LambdaQueryWrapper<OrganizationTable> queryWrapper= Wrappers.lambdaQuery();
        LambdaQueryWrapper<OrganizationTable> childQueryWrapper= Wrappers.lambdaQuery();
        List<OrganizationNotCountryDto> result=new ArrayList<>();
        //获取镇级
        queryWrapper.eq(OrganizationTable::getRank,1);
        List<OrganizationTable> organizationTables=mapper.selectList(queryWrapper);
        for(OrganizationTable item:organizationTables){
            OrganizationNotCountryDto organizationNotCountryDto=(OrganizationNotCountryDto) item;
            childQueryWrapper.eq(OrganizationTable::getParentId,item.getId());
            List<OrganizationTable> childs=mapper.selectList(childQueryWrapper);
            organizationNotCountryDto.setChilds(childs);
            childQueryWrapper.clear();
            result.add(organizationNotCountryDto);
        }
        return result;
    }
}