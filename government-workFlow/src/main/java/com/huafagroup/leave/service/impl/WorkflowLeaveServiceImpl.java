package com.huafagroup.leave.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.huafagroup.common.core.domain.entity.SysRole;
import com.huafagroup.common.utils.DateUtils;
import com.huafagroup.common.utils.SecurityUtils;
import com.huafagroup.common.utils.StringUtils;
import com.huafagroup.common.utils.uuid.UUID;
import com.huafagroup.leave.domain.WorkflowLeave;
import com.huafagroup.leave.mapper.WorkflowLeaveMapper;
import com.huafagroup.leave.service.IWorkflowLeaveService;
import com.huafagroup.system.domain.SysUserRole;
import com.huafagroup.system.mapper.SysUserRoleMapper;
import com.huafagroup.system.service.ISysRoleService;
import com.huafagroup.system.service.ISysUserService;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 请假Service业务层处理
 *
 * @author danny
 * @date 2020-10-28
 */
@Service
public class WorkflowLeaveServiceImpl implements IWorkflowLeaveService {

    @Autowired
    private WorkflowLeaveMapper workflowLeaveMapper;
    @Autowired
    private ProcessRuntime processRuntime;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * 查询请假
     *
     * @param id 请假ID
     * @return 请假
     */
    @Override
    public WorkflowLeave selectWorkflowLeaveById(String id) {
        return workflowLeaveMapper.selectWorkflowLeaveById(id);
    }

    /**
     * 查询请假列表
     *
     * @param workflowLeave 请假
     * @return 请假
     */
    @Override
    public List<WorkflowLeave> selectWorkflowLeaveList(WorkflowLeave workflowLeave) {
        return workflowLeaveMapper.selectWorkflowLeaveListByWorkflowLeaveAndDeptId(workflowLeave,SecurityUtils.getLoginUser().getUser().getDeptId());
    }
    /**
     * 查询请假列表带任务状态
     *
     * @param workflowLeave 请假
     * @return 请假
     */
    @Override
    public List<WorkflowLeave> selectWorkflowLeaveAndTaskNameList(WorkflowLeave workflowLeave) {
        List<WorkflowLeave> workflowLeaves = workflowLeaveMapper.selectWorkflowLeaveList(workflowLeave);
        List<String> collect = workflowLeaves.parallelStream().map(wl -> wl.getInstanceId()).collect(Collectors.toList());
        if(collect!=null&&!collect.isEmpty()) {
            List<Task> tasks = taskService.createTaskQuery().processInstanceIdIn(collect).list();
            workflowLeaves.forEach(
                    wl->{
                        Task task = tasks.parallelStream().filter(t -> t.getProcessInstanceId().equals(wl.getInstanceId())).findAny().orElse(null);
                        if (task != null) {
                            wl.setTaskName(task.getName());
                        }
                    }
            );
        }
        return workflowLeaves;
    }

    /**
     * 新增请假
     *
     * @param workflowLeave 请假
     * @return 结果
     */
    @Override
    public int insertWorkflowLeave(WorkflowLeave workflowLeave) {

        String id = UUID.randomUUID().toString();
        workflowLeave.setId(id);
        workflowLeave.setCreateTime(DateUtils.getNowDate());
        //获取部门

        String join = StringUtils.join(sysUserService.selectUserNameByPostCodeAndDeptId("se", SecurityUtils.getLoginUser().getUser().getDeptId()), ",");
/*        LambdaQueryWrapper<SysRole> roleQuery = Wrappers.lambdaQuery();
        roleQuery.eq(SysRole::getRoleName, "审批负责人");
        //获取审批负责人的角色实体
        SysRole role = roleService.getOne(roleQuery);
        List<SysUserRole> sysUserRoles
        String join*/
        ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
                .start()
                .withProcessDefinitionKey("Process_1")
                .withName(workflowLeave.getTitle())
                .withBusinessKey(id)
                //设置${deptLeader1}的值
                .withVariable("deptLeader1",join)
                .withVariable("deptLeader2",join)
                .build());
        workflowLeave.setInstanceId(processInstance.getId());
        workflowLeave.setState("0");
        workflowLeave.setCreateName(SecurityUtils.getNickName());
        workflowLeave.setCreateBy(SecurityUtils.getUsername());
        workflowLeave.setCreateTime(DateUtils.getNowDate());
        return workflowLeaveMapper.insertWorkflowLeave(workflowLeave);
    }

    /**
     * 修改请假
     *
     * @param workflowLeave 请假
     * @return 结果
     */
    @Override
    public int updateWorkflowLeave(WorkflowLeave workflowLeave) {
        workflowLeave.setUpdateTime(DateUtils.getNowDate());
        return workflowLeaveMapper.updateWorkflowLeave(workflowLeave);
    }

    /**
     * 批量删除请假
     *
     * @param ids 需要删除的请假ID
     * @return 结果
     */
    @Override
    public int deleteWorkflowLeaveByIds(String[] ids) {
        return workflowLeaveMapper.deleteWorkflowLeaveByIds(ids);
    }

    /**
     * 删除请假信息
     *
     * @param id 请假ID
     * @return 结果
     */
    @Override
    public int deleteWorkflowLeaveById(String id) {
        return workflowLeaveMapper.deleteWorkflowLeaveById(id);
    }

    @Override
    public WorkflowLeave selectWorkflowLeaveByInstanceId(String instanceId) {

        return workflowLeaveMapper.selectWorkflowLeaveByInstanceId(instanceId);
    }
}
