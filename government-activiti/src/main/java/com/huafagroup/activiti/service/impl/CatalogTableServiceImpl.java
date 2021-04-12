package com.huafagroup.activiti.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huafagroup.activiti.domain.dto.CatalogTableDto;
import com.huafagroup.activiti.entity.CatalogTable;
import com.huafagroup.activiti.entity.ModuleTable;
import com.huafagroup.activiti.mapper.CatalogTableMapper;
import com.huafagroup.activiti.service.CatalogTableService;
import com.huafagroup.common.utils.QueryDto;
import com.huafagroup.common.utils.SearchQueryEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<CatalogTableDto> findPageList(QueryDto queryDto) {
        LambdaQueryWrapper<CatalogTable> parentQueryWrapper= Wrappers.lambdaQuery();
        // 名称查询
        if (queryDto.getSearchValue().get(SearchQueryEnum.MODULE_ID.getValue()) != null) {
            parentQueryWrapper.eq(CatalogTable::getModuleId,queryDto.getSearchValue().get(SearchQueryEnum.MODULE_ID.getValue()));
        }
        // 页面类型查询
        if (queryDto.getSearchValue().get(SearchQueryEnum.PAGE_TYPE.getValue()) != null) {
            parentQueryWrapper.eq(CatalogTable::getPageType,queryDto.getSearchValue().get(SearchQueryEnum.PAGE_TYPE.getValue()));
        }
        parentQueryWrapper.eq(CatalogTable::getParentId,null);
        parentQueryWrapper.orderByAsc(CatalogTable::getSort);
        List<CatalogTable> parents=mapper.selectList(parentQueryWrapper);
        List<CatalogTableDto> catalogTableDtos=new ArrayList<>();
        LambdaQueryWrapper<CatalogTable> childQueryWrapper= Wrappers.lambdaQuery();
        for (CatalogTable item:parents){
            List<CatalogTable> childs;
            CatalogTableDto catalogTableDto=new CatalogTableDto();
            BeanUtils.copyProperties(item,catalogTableDto);
            childQueryWrapper.eq(CatalogTable::getParentId,item.getId())
                    .orderByAsc(CatalogTable::getSort);
            childs=mapper.selectList(childQueryWrapper);
            catalogTableDto.setChilds(childs);
            childQueryWrapper.clear();
        }

        return catalogTableDtos;
    }
}