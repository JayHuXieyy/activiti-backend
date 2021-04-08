package com.huafagroup.activiti.domain.dto;

import com.huafagroup.activiti.entity.OrganizationTable;

import java.util.List;

/**
 * @author ：linshenghui
 * @date ：2021/4/8 9:56
 * @return :$
 */
public class OrganizationNotCountryDto extends OrganizationTable {
    List<OrganizationTable> childs;

    public List<OrganizationTable> getChilds() {
        return childs;
    }

    public void setChilds(List<OrganizationTable> childs) {
        this.childs = childs;
    }

}
