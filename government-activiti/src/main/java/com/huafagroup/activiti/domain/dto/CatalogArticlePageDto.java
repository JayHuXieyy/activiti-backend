package com.huafagroup.activiti.domain.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huafagroup.activiti.entity.ArticleTable;
import com.huafagroup.activiti.entity.CatalogTable;
import com.huafagroup.activiti.entity.ModuleTable;
import com.huafagroup.activiti.entity.OrganizationTable;

import java.util.List;

/**
 * @author ：linshenghui
 * @date ：2021/4/8 14:41
 * @return :$
 */
public class CatalogArticlePageDto extends CatalogTable {
    List<CatalogArticlePageDto> childs;
    Page<ArticleTable> articleTables;

    public List<CatalogArticlePageDto> getChilds() {
        return childs;
    }

    public void setChilds(List<CatalogArticlePageDto> childs) {
        this.childs = childs;
    }

    public Page<ArticleTable> getArticleTables() {
        return articleTables;
    }

    public void setArticleTables(Page<ArticleTable> articleTables) {
        this.articleTables = articleTables;
    }
}
