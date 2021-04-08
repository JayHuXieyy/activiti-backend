package com.huafagroup.web.controller.activiti.biz;

import com.huafagroup.activiti.service.biz.BizTerminalService;
import com.huafagroup.common.constant.Constants;
import com.huafagroup.common.core.domain.AjaxResult;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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



}
