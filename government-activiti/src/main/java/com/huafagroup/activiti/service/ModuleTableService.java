package com.huafagroup.activiti.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.huafagroup.activiti.domain.dto.ModuleTableDto;
import com.huafagroup.activiti.entity.ModuleTable;
import com.huafagroup.common.utils.QueryDto;

import java.text.ParseException;
import java.util.List;

/**
* <p>
    * </p>
*
* @author 13738
* @date 2021-04-07 18:16:58
* @version
*/

public interface ModuleTableService extends IService<ModuleTable>{

    List<ModuleTableDto> findPageList(QueryDto queryDto) throws ParseException;
}