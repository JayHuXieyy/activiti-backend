package com.huafagroup.web.controller.activiti.biz;

import com.huafagroup.activiti.service.biz.BizTerminalService;
import com.huafagroup.common.constant.Constants;
import com.huafagroup.common.core.domain.AjaxResult;
import com.huafagroup.common.utils.QueryDto;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * </p>
 *
 * @author lsh
 */
@Api(tags = "终端接口")
@RestController
@RequestMapping(value = "/activiti/biz-terminal")
public class BizTerminalController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BizTerminalService service;

    @ApiOperation(value = "终端首页")
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public AjaxResult index(){
        return AjaxResult.success(service.index());
    }

    @ApiOperation(value = "更多模块")
    @RequestMapping(value = "/moreModule", method = RequestMethod.POST)
    public AjaxResult moreModule(@RequestParam Integer pageType){
        return AjaxResult.success(service.moreModule(pageType));
    }

}
