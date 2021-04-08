package com.huafagroup.activiti.service.biz;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huafagroup.activiti.domain.dto.BizTerminalIndexDto;
import com.huafagroup.activiti.domain.dto.BizTerminalArticleDto;
import com.huafagroup.activiti.entity.ArticleTable;
import com.huafagroup.activiti.entity.ModuleTable;
import com.huafagroup.activiti.entity.OrganizationTable;
import com.huafagroup.common.utils.QueryDto;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author jay
 * @date 2021-01-07 16:04:58
 */

public interface BizTerminalService {

    BizTerminalIndexDto index();

    List<ModuleTable> moreModule(Integer pageType);

    BizTerminalArticleDto info(QueryDto queryDto);

    Page<OrganizationTable> getAllTown(QueryDto queryDto);

    Page<OrganizationTable> getVillageByTown(QueryDto queryDto);

    ArticleTable articleInfo(Long articleId);
}