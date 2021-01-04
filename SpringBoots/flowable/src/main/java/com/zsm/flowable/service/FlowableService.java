package com.zsm.flowable.service;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @Author: zeng.
 * @Date:Created in 2020-12-30 17:24.
 * @Description:
 */
@Service
public class FlowableService
{
    /**
     * 流程运行控制服务
     */
    @Autowired
    private RuntimeService runtimeService;

    /**
     * 任务管理服务
     */
    @Autowired
    private TaskService taskService;

    /**
     * 流程引擎
     */
    @Autowired
    private ProcessEngine processEngine;

    /**
     * 注册流程服务
     */
    @Autowired
    private RepositoryService repositoryService;

    /**
     * 历史数据
     */
    @Autowired
    private HistoryService historyService;

    /**
     * 启动流程
     *
     * @param processKey  流程定义key(流程图ID)
     * @param businessKey 业务key
     * @param map         参数键值对
     * @return 流程实例
     */
    public ProcessInstance startProcessInstanceByKey(String processKey, String businessKey, HashMap<String, Object> map)
    {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, businessKey, map);
        return processInstance;
    }

    /**
     * 终止流程
     *
     * @param processInstanceId 流程实例ID
     * @param reason            终止理由
     */
    public void deleteProcessInstance(String processInstanceId, String reason)
    {
        runtimeService.deleteProcessInstance(processInstanceId, reason);
    }

    /**
     * 获取流程实例
     *
     * @param processId 流程Id
     * @return
     */
    public ProcessInstance getProcessByProcessId(String processId)
    {
        ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(
            processId).singleResult();
        return instance;
    }

    /**
     * 根据流程Id获取一个task任务
     *
     * @param processId 流程Id
     * @return
     */
    public Task getOneTaskByProcessId(String processId)
    {
        List<Task> list = taskService.createTaskQuery().processInstanceId(
            processId).orderByTaskCreateTime().desc().list();
        if (list.size() > 0)
        {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据任务Id获取一个task任务
     *
     * @param taskId 任务Id
     * @return
     */
    public Task getOneTaskByTaskId(String taskId)
    {
        List<Task> list = taskService.createTaskQuery().taskId(taskId).orderByTaskCreateTime().desc().list();
        if (list.size() > 0)
        {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据流程Id获取一个taskId任务Id
     *
     * @param processId 流程Id
     * @return
     */
    public String getOneTaskIdByProcessId(String processId)
    {
        List<Task> list = taskService.createTaskQuery().processInstanceId(
            processId).orderByTaskCreateTime().desc().list();
        if (list.size() > 0)
        {
            return list.get(0).getId();
        }
        return null;
    }

    /**
     * 根据流程Id获取所有taskId任务Id
     *
     * @param processId 流程Id
     * @return
     */
    public List<String> getTaskIdsByProcessId(String processId)
    {
        List<Task> list = taskService.createTaskQuery().processInstanceId(
            processId).orderByTaskCreateTime().desc().list();
        List<String> res = new ArrayList<>();
        for (Task task : list)
        {
            res.add(task.getId());
        }
        return res;
    }

    /**
     * 获取指定用户的任务列表（创建时间倒序）
     *
     * @param userId 用户ID
     * @return 任务列表
     */
    public List<Task> getTasksByUserId(String userId)
    {
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(userId).orderByTaskCreateTime().desc().list();
        System.out.println(userId + "用户任务列表：" + tasks.size());
        for (Task task : tasks)
        {
            System.out.println(task.toString());
        }
        return tasks;
    }

    /**
     * 获取指定用户组的任务列表
     *
     * @param group 用户组
     * @return 任务列表
     */
    public List<Task> getTaskByGroup(String group)
    {
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(group)
            .orderByTaskCreateTime().desc().list();
        System.out.println(group + "用户组任务列表：" + tasks.size());
        for (Task task : tasks)
        {
            System.out.println(task.toString());
        }
        return tasks;
    }

    /**
     * 获取指定业务key的任务列表
     *
     * @param businessKey
     * @return
     */
    public List<Task> getTasksByBusinessKey(String businessKey)
    {
        List<Task> list = taskService.createTaskQuery().processInstanceBusinessKey(
            businessKey).orderByTaskCreateTime().desc().list();
        return list;
    }

    /**
     * 完成指定任务
     *
     * @param taskId 任务ID
     * @param map    变量键值对
     */
    public void complete(String taskId, HashMap<String, Object> map)
    {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null)
        {
            throw new IllegalArgumentException("流程不存在");
        }
        taskService.complete(taskId, map);
    }

    /**
     * 回退或指向指定流程
     *
     * @param processId
     * @param targetTaskId
     * @param taskKeys
     */
    public void changeActivityState(String processId, String targetTaskId, List<String> taskKeys)
    {
        runtimeService.createChangeActivityStateBuilder().processInstanceId(processId)
            .moveActivityIdsToSingleActivityId(taskKeys, targetTaskId);
    }

    /**
     * 获取当前流程执行实例
     *
     * @param processId
     * @return
     */
    public List<Execution> getExecutions(String processId)
    {
        List<Execution> list = runtimeService.createExecutionQuery().processInstanceId(processId).list();
        return list;
    }

    /**
     * 获取当前流程执行激活的实例id
     *
     * @param executionId
     * @return
     */
    public List<String> getActiveActivityIds(String executionId)
    {
        List<String> ids = runtimeService.getActiveActivityIds(executionId);
        return ids;
    }

    /**
     * 获取当前流程 BPMN 流程模型
     *
     * @param processDefinitionId
     * @return
     */
    public BpmnModel getBpmnModel(String processDefinitionId)
    {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        return bpmnModel;
    }

    /**
     * 获取流程配置引擎
     *
     * @return
     */
    public ProcessEngineConfiguration getProcessEngineConfiguration()
    {
        ProcessEngineConfiguration configuration = processEngine.getProcessEngineConfiguration();
        return configuration;
    }

    /**
     * 获取指定任务列表中的特定任务
     *
     * @param list        任务列表
     * @param businessKey 业务key
     * @return 任务
     */
    public Task getOneByBusinessKey(@NotNull List<Task> list, String businessKey)
    {
        Task task = null;
        for (Task t : list)
        {
            // 通过任务对象获取流程实例
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(
                t.getProcessInstanceId()).singleResult();
            if (businessKey.equals(pi.getBusinessKey()))
            {
                task = t;
            }
        }
        return task;
    }

    /**
     * 创建流程并完成第一个任务
     *
     * @param processKey  流程定义key(流程图ID)
     * @param businessKey 业务key
     * @param map         变量键值对
     */
    public void startAndComplete(String processKey, String businessKey, HashMap<String, Object> map)
    {
        ProcessInstance instance = startProcessInstanceByKey(processKey, businessKey, map);
        Task task = processEngine.getTaskService().createTaskQuery().processInstanceId(instance.getId()).singleResult();
        taskService.complete(task.getId(), map);
    }

    /**
     * 退回到指定任务节点
     *
     * @param currentTaskId 当前任务ID
     * @param targetTaskKey 目标任务节点key
     */
    public void backToStep(String currentTaskId, String targetTaskKey)
    {
        Task currentTask = taskService.createTaskQuery().taskId(currentTaskId).singleResult();
        if (currentTask == null)
        {
            throw new IllegalArgumentException("当前任务节点不存在");
        }
        List<String> currentTaskKeys = new ArrayList<>();
        currentTaskKeys.add(currentTask.getTaskDefinitionKey());
        runtimeService.createChangeActivityStateBuilder().processInstanceId(
            currentTask.getProcessInstanceId()).moveActivityIdsToSingleActivityId(currentTaskKeys, targetTaskKey);
    }

    /**
     * 获取指定用户下面一个任务
     *
     * @param userId 用户ID
     * @return
     */
    public Task getOneTask(List<Task> list, String userId)
    {
        for (Task task : list)
        {
            if (task.getAssignee().equals(userId))
            {
                return task;
            }
        }
        return null;
    }

    /**
     * 设置流程变量
     *
     * @param processId    流程id
     * @param variableName 变量名称
     * @param value        变量值
     */
    public void setProcessVariable(String processId, String variableName, Object value)
    {
        runtimeService.setVariable(processId, variableName, value);
    }

    /**
     * 获取流程变量
     *
     * @param processId    流程id
     * @param variableName 变量名称
     */
    public Object getProcessVariable(String processId, String variableName)
    {
        Object variable = runtimeService.getVariable(processId, variableName);
        return variable;
    }

    /**
     * 获取历史流程实例
     *
     * @param processId
     * @return
     */
    public HistoricProcessInstance getHistoricProcessInstance(String processId)
    {
        HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery().processInstanceId(
            processId).singleResult();
        return instance;
    }

    /**
     * 获取历史任务实例
     *
     * @param taskId
     * @return
     */
    public HistoricTaskInstance getHistoricTaskInstance(String taskId)
    {
        HistoricTaskInstance instance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        return instance;
    }

    /**
     * 获取历史任务实例
     *
     * @param processId
     * @return
     */
    public List<HistoricTaskInstance> getHistoricTaskInstances(String processId)
    {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(
            processId).orderByTaskCreateTime().asc().list();
        return list;
    }

    /**
     * 获取历史任务实例(根据节点名称)
     *
     * @param processId
     * @param nodeName
     * @return
     */
    public List<HistoricTaskInstance> getHistoricTaskInstances(String processId, String nodeName)
    {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(
            processId).taskName(nodeName).orderByTaskCreateTime().asc().list();
        return list;
    }

    /**
     * 获取历史任务实例(根据用户id)
     *
     * @param userId
     * @return
     */
    public List<HistoricTaskInstance> getHistoricTaskInstancesByTaskAssignee(String userId)
    {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().taskAssignee(
            userId).finished().orderByTaskCreateTime().desc().list();
        return list;
    }
}
