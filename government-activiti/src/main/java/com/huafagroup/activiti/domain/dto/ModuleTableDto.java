package com.huafagroup.activiti.domain.dto;

import com.huafagroup.activiti.entity.ModuleTable;

/**
 * @author ：linshenghui
 * @date ：2021/4/8 9:49
 * @return :$
 */
public class ModuleTableDto extends ModuleTable {
    private String deptName;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
