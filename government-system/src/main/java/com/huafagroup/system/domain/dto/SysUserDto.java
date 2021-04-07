package com.huafagroup.system.domain.dto;

import com.huafagroup.common.core.domain.entity.SysRole;
import com.huafagroup.common.core.domain.entity.SysUser;

import java.util.List;

/**
 * @author ：linshenghui
 * @date ：2021/4/7 18:31
 * @return :$
 */
public class SysUserDto extends SysUser {
    private List<SysRole> roleList;

    public List<SysRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<SysRole> roleList) {
        this.roleList = roleList;
    }
}
