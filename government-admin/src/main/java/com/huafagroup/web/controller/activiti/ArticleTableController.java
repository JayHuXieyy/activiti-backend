package com.huafagroup.web.controller.activiti;

import com.huafagroup.activiti.entity.ArticleTable;
import com.huafagroup.activiti.service.ArticleTableService;
import com.huafagroup.common.constant.Constants;
import com.huafagroup.common.core.domain.AjaxResult;
import com.huafagroup.common.utils.QueryDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * </p>
 *
 * @author 13738
 * @date 2021-04-07 18:16:20
 */
@Api(tags = "文章接口")
@RestController
@RequestMapping(value = "/activiti/article-table")
public class ArticleTableController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ArticleTableService service;


    @ApiOperation(value = "获取单条记录", notes = "根据url的id来获取详细信息")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public AjaxResult get(String id) {
        ArticleTable item = service.getById(id);
        if (item != null) {
            return AjaxResult.success(item);
        } else {
            return AjaxResult.error("找不到该记录");
        }
    }

    @ApiOperation(value = "管理后台查询列表")
    @RequestMapping(value = "/findpagelist", method = RequestMethod.POST)
    public AjaxResult findPageList(@RequestBody @Validated QueryDto queryDto) throws Exception {
        return AjaxResult.success(service.findPageList(queryDto));
    }

    @ApiOperation(value = "获取列表记录")
    @RequestMapping(value = "/getlist", method = RequestMethod.GET)
    public AjaxResult getList() {
        return AjaxResult.success(service.list());
    }

    @ApiOperation(value = "新增")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public AjaxResult create(@RequestBody @Validated ArticleTable item) {
        item.setActivitiStatus(Constants.status_0);
        item.setDelFlag(Constants.status_0);
        boolean bl = service.save(item);
        if (bl) {
            return AjaxResult.success("新增成功");
        } else {
            return AjaxResult.error("新增失败");
        }
    }

    @ApiOperation(value = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public AjaxResult update(@RequestBody @Validated ArticleTable item) {
        boolean bl = service.updateById(item);
        if (bl) {
            return AjaxResult.success("更新成功");
        } else {
            return AjaxResult.error("更新失败");
        }
    }

    @ApiOperation(value = "按ID删除")
    @RequestMapping(value = "/deletebyid", method = RequestMethod.POST)
    public AjaxResult delete(String id) {
        boolean bl = service.removeById(id);
        if (bl) {
            return AjaxResult.success("删除成功");
        } else {
            return AjaxResult.error("删除失败");
        }
    }

    @ApiOperation(value = "发起审批")
    @RequestMapping(value = "/launch", method = RequestMethod.POST)
    public AjaxResult launch(String id) {
        boolean bl = service.launch(id);
        if (bl) {
            return AjaxResult.success("删除成功");
        } else {
            return AjaxResult.error("删除失败");
        }
    }
}
