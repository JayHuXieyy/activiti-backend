package com.huafagroup.activiti.domain.dto;

import com.huafagroup.activiti.entity.OrganizationTable;

import java.util.List;

/**
 * @author ：linshenghui
 * @date ：2021/4/13 15:30
 * @return :$
 */
public class OrganizationDto extends OrganizationTable {
    private List<OrganizationDto> childs;

    public List<OrganizationDto> getChilds() {
        return childs;
    }

    public void setChilds(List<OrganizationDto> childs) {
        this.childs = childs;
    }
}
