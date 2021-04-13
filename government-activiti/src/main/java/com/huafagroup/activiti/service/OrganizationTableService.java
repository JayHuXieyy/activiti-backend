package com.huafagroup.activiti.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.huafagroup.activiti.domain.dto.OrganizationDto;
import com.huafagroup.activiti.domain.dto.OrganizationNotCountryDto;
import com.huafagroup.activiti.entity.OrganizationTable;
import com.huafagroup.common.utils.QueryDto;

import java.text.ParseException;
import java.util.List;

/**
* <p>
    * </p>
*
* @author 13738
* @date 2021-04-07 18:17:08
* @version
*/

public interface OrganizationTableService extends IService<OrganizationTable>{

    /**
     * 管理后台查询列表
     *
     * @return 楼层列表
     */
    List<OrganizationDto> findPageList(QueryDto queryDto) throws ParseException;

    Boolean delete(String id);

    List<OrganizationNotCountryDto> getlistNotcounty();
}