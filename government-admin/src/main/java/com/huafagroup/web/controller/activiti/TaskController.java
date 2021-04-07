package com.huafagroup.web.controller.activiti;


import com.github.pagehelper.Page;
import com.huafagroup.activiti.domain.dto.ActTaskDTO;
import com.huafagroup.activiti.domain.dto.ActWorkflowFormDataDTO;
import com.huafagroup.activiti.service.IActTaskService;
import com.huafagroup.common.core.controller.BaseController;
import com.huafagroup.common.core.domain.AjaxResult;
import com.huafagroup.common.core.page.PageDomain;
import com.huafagroup.common.core.page.TableDataInfo;
import com.huafagroup.common.core.page.TableSupport;
import org.activiti.api.task.runtime.TaskRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;


@RestController
@RequestMapping("/task")
public class TaskController extends BaseController {
    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private IActTaskService actTaskService;



    //获取我的代办任务
    @GetMapping(value = "/list")
    public TableDataInfo getTasks() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<ActTaskDTO> hashMaps = actTaskService.selectProcessDefinitionList(pageDomain);
         return getDataTable(hashMaps);


    }


    //渲染表单
    @GetMapping(value = "/formDataShow/{taskID}")
    public AjaxResult formDataShow(@PathVariable("taskID") String taskID) {

        return AjaxResult.success(actTaskService.formDataShow(taskID));
    }

    //保存表单
    @PostMapping(value = "/formDataSave/{taskID}")
    public AjaxResult formDataSave(@PathVariable("taskID") String taskID,
                                   @RequestBody   List<ActWorkflowFormDataDTO> formData ) throws ParseException {
        return toAjax(actTaskService.formDataSave(taskID, formData));

    }

}
