package com.huafagroup.activiti.domain.dto;

import com.huafagroup.activiti.entity.ArticleTable;
import com.huafagroup.activiti.entity.CatalogTable;

import java.util.List;

/**
 * @author ：linshenghui
 * @date ：2021/4/8 9:56
 * @return :$
 */
public class CatalogArticleDto extends CatalogTable {
    List<CatalogArticleDto> childs;
    List<ArticleTable> articleTables;

    public List<CatalogArticleDto> getChilds() {
        return childs;
    }

    public void setChilds(List<CatalogArticleDto> childs) {
        this.childs = childs;
    }

    public List<ArticleTable> getArticleTables() {
        return articleTables;
    }

    public void setArticleTables(List<ArticleTable> articleTables) {
        this.articleTables = articleTables;
    }
}
