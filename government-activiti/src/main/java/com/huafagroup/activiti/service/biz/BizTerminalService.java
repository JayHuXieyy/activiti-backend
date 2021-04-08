package com.huafagroup.activiti.service.biz;


import com.huafagroup.activiti.domain.dto.BizTerminalIndexDto;
import com.huafagroup.activiti.entity.ModuleTable;

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
}