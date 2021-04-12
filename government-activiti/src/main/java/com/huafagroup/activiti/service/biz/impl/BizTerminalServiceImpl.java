package com.huafagroup.activiti.service.biz.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huafagroup.activiti.domain.dto.BizTerminalIndexDto;
import com.huafagroup.activiti.domain.dto.BizTerminalArticleDto;
import com.huafagroup.activiti.domain.dto.CatalogArticleDto;
import com.huafagroup.activiti.domain.dto.CatalogArticlePageDto;
import com.huafagroup.activiti.entity.ArticleTable;
import com.huafagroup.activiti.entity.CatalogTable;
import com.huafagroup.activiti.entity.ModuleTable;
import com.huafagroup.activiti.entity.OrganizationTable;
import com.huafagroup.activiti.service.ArticleTableService;
import com.huafagroup.activiti.service.CatalogTableService;
import com.huafagroup.activiti.service.ModuleTableService;
import com.huafagroup.activiti.service.OrganizationTableService;
import com.huafagroup.activiti.service.biz.BizTerminalService;

import com.huafagroup.common.constant.Constants;
import com.huafagroup.common.utils.ModulePageTypeEnum;
import com.huafagroup.common.utils.QueryDto;
import com.huafagroup.common.utils.SearchQueryEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class BizTerminalServiceImpl implements BizTerminalService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ModuleTableService moduleTableService;
    @Autowired
    private CatalogTableService catalogTableService;
    @Autowired
    private ArticleTableService articleTableService;
    @Autowired
    private OrganizationTableService organizationTableService;

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
        LambdaQueryWrapper<ModuleTable> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ModuleTable::getPageType, pageType)
                .eq(ModuleTable::getDelFlag, Constants.status_0)
                .orderByAsc(ModuleTable::getSort);
        List<ModuleTable> moduleTables = moduleTableService.list(queryWrapper);
        return moduleTables;
    }

    @Override
    public BizTerminalArticleDto info(QueryDto queryDto) {
        BizTerminalArticleDto bizTerminalArticleDto;
        Long moduleId = Long.valueOf(Long.valueOf(queryDto.getSearchValue().get(SearchQueryEnum.MODULE_ID.getValue()).toString()));
        Object organizationIdObject = queryDto.getSearchValue().get(SearchQueryEnum.ORGANIZATION_ID.getValue());
        // 页面类型查询
        if (queryDto.getSearchValue().get(SearchQueryEnum.PAGE_TYPE.getValue()).equals(ModulePageTypeEnum.PARTY.getValue())) {
            //党组织模块
            // 根据组织id判断页面
            if (organizationIdObject == null) {
                //模块详细
                bizTerminalArticleDto = this.moduleInfo(moduleId);
                bizTerminalArticleDto.setDynamics(this.dynamics(moduleId));
            } else {
                //镇村详细
                Long organizationId = Long.valueOf(organizationIdObject.toString());
                bizTerminalArticleDto = this.townVillageInfo(organizationId);
                bizTerminalArticleDto.setArticles(this.articles(moduleId, organizationId, queryDto.getPageNo(), queryDto.getPageSize()));
            }
        } else {
            bizTerminalArticleDto = this.moduleInfo(moduleId);
            bizTerminalArticleDto.setDynamics(this.dynamics(moduleId));
            bizTerminalArticleDto.setArticles(this.articles(moduleId, null, queryDto.getPageNo(), queryDto.getPageSize()));
        }


        return bizTerminalArticleDto;
    }

    /*模块详细*/
    private BizTerminalArticleDto moduleInfo(Long moduleId) {
        ModuleTable moduleTable = moduleTableService.getById(moduleId);
        BizTerminalArticleDto bizTerminalModuleDto = new BizTerminalArticleDto();
        bizTerminalModuleDto.setModuleTable(moduleTable);

        return bizTerminalModuleDto;
    }

    /*镇村详细*/
    private BizTerminalArticleDto townVillageInfo(Long organizationId) {
        OrganizationTable organizationTable = organizationTableService.getById(organizationId);
        BizTerminalArticleDto bizTerminalModuleDto = new BizTerminalArticleDto();
        bizTerminalModuleDto.setOrganizationTable(organizationTable);

        return bizTerminalModuleDto;
    }

    /*动态目录和文章*/
    private List<CatalogArticleDto> dynamics(Long moduleId) {
        //获取父目录
        LambdaQueryWrapper<CatalogTable> parentQueryWrapper = Wrappers.lambdaQuery();
        parentQueryWrapper.eq(CatalogTable::getModuleId, moduleId)
                .eq(CatalogTable::getPageType, Constants.status_0)
                .eq(CatalogTable::getParentId, null)
                .eq(CatalogTable::getDelFlag, Constants.status_0)
                .orderByAsc(CatalogTable::getSort);
        List<CatalogTable> parents = catalogTableService.list(parentQueryWrapper);
        List<CatalogArticleDto> catalogArticleDtos = new ArrayList<>();
        LambdaQueryWrapper<CatalogTable> childQueryWrapper = Wrappers.lambdaQuery();
        LambdaQueryWrapper<ArticleTable> articleQueryWrapper = Wrappers.lambdaQuery();
        for (CatalogTable item : parents) {
            //父类转子类
            CatalogArticleDto catalogArticleDto = (CatalogArticleDto) item;
            //获取父目录文章(动态目录独有)
            articleQueryWrapper.eq(ArticleTable::getCatalogId, item.getId());
            List<ArticleTable> parentArticles = articleTableService.list(articleQueryWrapper);
            catalogArticleDto.setArticleTables(parentArticles);
            articleQueryWrapper.clear();
            //获取子目录
            List<CatalogTable> childsData;
            List<CatalogArticleDto> dynamicsChilds = new ArrayList<>();
            childQueryWrapper.eq(CatalogTable::getParentId, item.getId())
                    .eq(CatalogTable::getDelFlag, Constants.status_0)
                    .orderByAsc(CatalogTable::getSort);
            childsData = catalogTableService.list(childQueryWrapper);
            if (childsData != null) {
                //将元素转化为子类
                for (CatalogTable catalogTable : childsData) {
                    CatalogArticleDto childDto = new CatalogArticleDto();
                    BeanUtils.copyProperties(catalogTable, childDto);
                    //获取子目录文章
                    articleQueryWrapper.eq(ArticleTable::getCatalogId, catalogTable.getId())
                            .eq(ArticleTable::getDelFlag, Constants.status_0);
                    List<ArticleTable> childArticles = articleTableService.list(articleQueryWrapper);
                    childDto.setArticleTables(childArticles);
                    dynamicsChilds.add(childDto);
                    articleQueryWrapper.clear();
                }
            }
            catalogArticleDto.setChilds(dynamicsChilds);
            childQueryWrapper.clear();
            catalogArticleDtos.add(catalogArticleDto);
        }

        return catalogArticleDtos;
    }

    /*内容目录和文章*/
    private List<CatalogArticlePageDto> articles(Long moduleId, Long organizationId, int pageNo, int pageSize) {
        //获取父目录
        LambdaQueryWrapper<CatalogTable> parentQueryWrapper = Wrappers.lambdaQuery();
        parentQueryWrapper.eq(CatalogTable::getModuleId, moduleId)
                .eq(CatalogTable::getPageType, Constants.status_0)
                .eq(CatalogTable::getParentId, null)
                .eq(CatalogTable::getDelFlag, Constants.status_0)
                .orderByAsc(CatalogTable::getSort);
        List<CatalogTable> parents = catalogTableService.list(parentQueryWrapper);
        List<CatalogArticlePageDto> catalogArticlePageDtos = new ArrayList<>();
        LambdaQueryWrapper<CatalogTable> childQueryWrapper = Wrappers.lambdaQuery();
        LambdaQueryWrapper<ArticleTable> articleQueryWrapper = Wrappers.lambdaQuery();
        for (CatalogTable item : parents) {
            //父类转子类
            CatalogArticlePageDto catalogArticlePageDto = (CatalogArticlePageDto) item;
            //获取子目录
            List<CatalogTable> childsData;
            List<CatalogArticlePageDto> townVillageChilds = new ArrayList<>();
            childQueryWrapper.eq(CatalogTable::getParentId, item.getId())
                    .eq(CatalogTable::getDelFlag, Constants.status_0)
                    .orderByAsc(CatalogTable::getSort);
            childsData = catalogTableService.list(childQueryWrapper);
            if (childsData != null) {
                //将元素转化为子类
                for (CatalogTable catalogTable : childsData) {
                    CatalogArticlePageDto childDto = new CatalogArticlePageDto();
                    BeanUtils.copyProperties(catalogTable, childDto);
                    //获取子目录文章
                    articleQueryWrapper.eq(ArticleTable::getCatalogId, catalogTable.getId())
                            .eq(ArticleTable::getDelFlag, Constants.status_0);
                    if (organizationId != null) {
                        articleQueryWrapper.eq(ArticleTable::getOrganizationId, organizationId);
                    }
                    Page<ArticleTable> childArticles = articleTableService.page(new Page<>(pageNo, pageSize), articleQueryWrapper);
                    childDto.setArticleTables(childArticles);
                    townVillageChilds.add(childDto);
                    articleQueryWrapper.clear();
                }
            }
            catalogArticlePageDto.setChilds(townVillageChilds);
            childQueryWrapper.clear();
            catalogArticlePageDtos.add(catalogArticlePageDto);
        }

        return catalogArticlePageDtos;
    }

    @Override
    public Page<OrganizationTable> getAllTown(QueryDto queryDto) {
        LambdaQueryWrapper<OrganizationTable> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(OrganizationTable::getRank, 1)
                .eq(OrganizationTable::getDelFlag, Constants.status_0)
                .orderByAsc(OrganizationTable::getSort);
        Page<OrganizationTable> towns = organizationTableService.page(new Page<>(queryDto.getPageNo(), queryDto.getPageSize()), queryWrapper);
        return towns;
    }

    @Override
    public Page<OrganizationTable> getVillageByTown(QueryDto queryDto) {
        LambdaQueryWrapper<OrganizationTable> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(OrganizationTable::getRank, 1)
                .eq(OrganizationTable::getParentId, queryDto.getSearchValue().get(SearchQueryEnum.ORGANIZATION_ID.getValue()))
                .eq(OrganizationTable::getDelFlag, Constants.status_0)
                .orderByAsc(OrganizationTable::getSort);
        Page<OrganizationTable> villages = organizationTableService.page(new Page<>(queryDto.getPageNo(), queryDto.getPageSize()), queryWrapper);
        return villages;
    }

    @Override
    public ArticleTable articleInfo(Long articleId) {
        return articleTableService.getById(articleId);
    }
}
