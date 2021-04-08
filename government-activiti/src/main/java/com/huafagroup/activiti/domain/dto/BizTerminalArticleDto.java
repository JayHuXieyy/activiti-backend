package com.huafagroup.activiti.domain.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huafagroup.activiti.entity.ModuleTable;
import com.huafagroup.activiti.entity.OrganizationTable;

import java.util.List;

/**
 * @author ：linshenghui
 * @date ：2021/4/8 14:41
 * @return :$
 */
public class BizTerminalArticleDto{

    private ModuleTable moduleTable;

    private OrganizationTable organizationTable;

    /*动态目录*/
    private List<CatalogArticleDto> dynamics;
    /*内容目录*/
    private List<CatalogArticlePageDto> articles;

    public List<CatalogArticleDto> getDynamics() {
        return dynamics;
    }

    public void setDynamics(List<CatalogArticleDto> dynamics) {
        this.dynamics = dynamics;
    }

    public List<CatalogArticlePageDto> getArticles() {
        return articles;
    }

    public void setArticles(List<CatalogArticlePageDto> articles) {
        this.articles = articles;
    }

    public ModuleTable getModuleTable() {
        return moduleTable;
    }

    public void setModuleTable(ModuleTable moduleTable) {
        this.moduleTable = moduleTable;
    }

    public OrganizationTable getOrganizationTable() {
        return organizationTable;
    }

    public void setOrganizationTable(OrganizationTable organizationTable) {
        this.organizationTable = organizationTable;
    }
}
