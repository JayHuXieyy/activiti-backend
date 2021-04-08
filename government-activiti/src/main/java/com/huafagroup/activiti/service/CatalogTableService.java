package com.huafagroup.activiti.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.huafagroup.activiti.domain.dto.CatalogTableDto;
import com.huafagroup.activiti.entity.CatalogTable;
import com.huafagroup.common.utils.QueryDto;

import java.util.List;

/**
* <p>
    * </p>
*
* @author 13738
* @date 2021-04-07 18:16:44
* @version
*/

public interface CatalogTableService extends IService<CatalogTable>{

    List<CatalogTableDto> findPageList(QueryDto queryDto);
}