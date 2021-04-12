package com.huafagroup.activiti.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huafagroup.activiti.domain.dto.CatalogArticleDto;
import com.huafagroup.activiti.domain.dto.CatalogTableDto;
import com.huafagroup.activiti.entity.ArticleTable;
import com.huafagroup.activiti.entity.CatalogTable;
import com.huafagroup.activiti.entity.OrganizationTable;
import com.huafagroup.activiti.mapper.ArticleTableMapper;
import com.huafagroup.activiti.mapper.CatalogTableMapper;
import com.huafagroup.activiti.mapper.OrganizationTableMapper;
import com.huafagroup.activiti.service.ArticleTableService;
import com.huafagroup.common.constant.Constants;
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
 * @date 2021-04-07 18:16:20
 */

@Service
public class ArticleTableServiceImpl extends ServiceImpl
        <ArticleTableMapper, ArticleTable> implements ArticleTableService {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ArticleTableMapper mapper;
    @Autowired
    private CatalogTableServiceImpl catalogTableService;
    @Autowired
    private OrganizationTableServiceImpl organizationTableService;

    @Override
    public List<CatalogArticleDto> findPageList(QueryDto queryDto) {
        LambdaQueryWrapper<CatalogTable> parentQueryWrapper = Wrappers.lambdaQuery();
        OrganizationTable organizationTable = null;
        // 名称查询
        if (queryDto.getSearchValue().get(SearchQueryEnum.MODULE_ID.getValue()) != null) {
            parentQueryWrapper.eq(CatalogTable::getModuleId, queryDto.getSearchValue().get(SearchQueryEnum.MODULE_ID.getValue()));
        }
        // 页面类型查询
        if (queryDto.getSearchValue().get(SearchQueryEnum.PAGE_TYPE.getValue()) != null) {
            parentQueryWrapper.eq(CatalogTable::getPageType, queryDto.getSearchValue().get(SearchQueryEnum.PAGE_TYPE.getValue()));
        }
        // 组织id查询
        if (queryDto.getSearchValue().get(SearchQueryEnum.ORGANIZATION_ID.getValue()) != null) {
            organizationTable = organizationTableService.getById(SearchQueryEnum.ORGANIZATION_ID.getValue());
            //判断组织的级别
            if (organizationTable.getRank() == 1) {
                //镇
                parentQueryWrapper.in(CatalogTable::getType, 1, 3);
            } else if (organizationTable.getRank() == 2) {
                //村
                parentQueryWrapper.in(CatalogTable::getType, 2, 3);
            }
        }
        parentQueryWrapper.eq(CatalogTable::getParentId, null)
                .eq(CatalogTable::getDelFlag, Constants.status_0)
                .orderByAsc(CatalogTable::getSort);
        List<CatalogTable> parents = catalogTableService.list(parentQueryWrapper);
        List<CatalogArticleDto> catalogArticleDtos = new ArrayList<>();
        LambdaQueryWrapper<CatalogTable> childQueryWrapper = Wrappers.lambdaQuery();
        LambdaQueryWrapper<ArticleTable> articleQueryWrapper = Wrappers.lambdaQuery();
        for (CatalogTable item : parents) {
            //父类转子类
            CatalogArticleDto catalogArticleDto = (CatalogArticleDto) item;
            List<CatalogTable> childsData;
            //子目录
            List<CatalogArticleDto> childs = new ArrayList<>();
            //获取父目录文章(只有动态目录才有)
            if (queryDto.getSearchValue().get(SearchQueryEnum.PAGE_TYPE.getValue()).equals("0")) {
                articleQueryWrapper.eq(ArticleTable::getCatalogId, item.getId());
                List<ArticleTable> parentArticles = mapper.selectList(articleQueryWrapper);
                catalogArticleDto.setArticleTables(parentArticles);
                articleQueryWrapper.clear();
            }
            //子目录下的文章
            List<ArticleTable> childArticles = new ArrayList<>();

            //获取子目录
            childQueryWrapper.eq(CatalogTable::getParentId, item.getId())
                    .eq(CatalogTable::getDelFlag, Constants.status_0)
                    .orderByAsc(CatalogTable::getSort);
            childsData = catalogTableService.list(childQueryWrapper);
            if (childsData != null) {
                //将元素转化为子类
                for (CatalogTable catalogTable : childsData) {
                    CatalogArticleDto childDto = new CatalogArticleDto();
                    BeanUtils.copyProperties(catalogTable,childDto);
                    //获取子目录文章
                    articleQueryWrapper.eq(ArticleTable::getCatalogId, catalogTable.getId())
                            .eq(ArticleTable::getDelFlag, Constants.status_0);
                    if (queryDto.getSearchValue().get(SearchQueryEnum.PAGE_TYPE.getValue()).equals("1")) {
                        //如果为非动态目录，需要筛选组织id
                        articleQueryWrapper.eq(ArticleTable::getOrganizationId,queryDto.getSearchValue().get(SearchQueryEnum.ORGANIZATION_ID.getValue()));
                    }
                    childArticles = mapper.selectList(articleQueryWrapper);
                    childDto.setArticleTables(childArticles);
                    childs.add(childDto);
                    articleQueryWrapper.clear();
                }
            }
            catalogArticleDto.setChilds(childs);
            childQueryWrapper.clear();
            catalogArticleDtos.add(catalogArticleDto);
        }

        return catalogArticleDtos;
    }

    @Override
    public Boolean launch(String id) {
        LambdaUpdateWrapper<ArticleTable> updateWrapper=Wrappers.lambdaUpdate();
        updateWrapper.set(ArticleTable::getId,id)
                .set(ArticleTable::getActivitiStatus,Constants.status_1);
        return this.update(updateWrapper);
    }
}