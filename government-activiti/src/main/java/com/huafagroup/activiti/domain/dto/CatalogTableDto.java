package com.huafagroup.activiti.domain.dto;

import com.huafagroup.activiti.entity.ArticleTable;
import com.huafagroup.activiti.entity.CatalogTable;

import java.util.List;

/**
 * @author ：linshenghui
 * @date ：2021/4/8 9:56
 * @return :$
 */
public class CatalogTableDto extends CatalogTable {
    List<CatalogTable> childs;

    public List<CatalogTable> getChilds() {
        return childs;
    }

    public void setChilds(List<CatalogTable> childs) {
        this.childs = childs;
    }
}
