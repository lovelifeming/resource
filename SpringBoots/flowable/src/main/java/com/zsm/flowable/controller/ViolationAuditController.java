package com.zsm.flowable.controller;

import com.zsm.flowable.util.ResultSet;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.common.impl.identity.Authentication;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceQuery;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


/**
 * @Author: zeng.
 * @Date:Created in 2020-12-08 15:38.
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/violation/")
public class ViolationAuditController
{
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ProcessEngine processEngine;

    @RequestMapping("start")
    @ResponseBody
    public ResultSet startProcess(@NotNull @NotEmpty String userId, @NotNull @NotEmpty String nextUserId,
                                  @NotNull @NotEmpty String approve)
    {
        log.info("startProcess创建流程violationProcess，userId={}，nextUserId={}，approve={}", userId, nextUserId, approve);
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("approve", approve);
        Authentication.setAuthenticatedUserId(userId);
        String businessKey = "violationProcess" + UUID.randomUUID().toString().replace("-", "");
        System.out.println("businessKey=" + businessKey);
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("violationProcess", businessKey, map);
        Authentication.setAuthenticatedUserId(null);

        String id = instance.getId();
        String taskId = taskService.createTaskQuery().processInstanceId(id).singleResult().getId();
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("userId", nextUserId);
        map1.put("approve", approve);
        log.info("新创建流程secureaudit，taskId={}", taskId);
        System.out.println("新创建流程secureaudit，taskId=" + taskId);

        taskService.complete(taskId, map1);
        Task task = taskService.createTaskQuery().processInstanceId(id).singleResult();
        task.setAssignee(nextUserId);
        String taskId1 = task.getId();
        ResultSet<String> res = ResultSet.success(taskId1);
        return res;
    }

    @RequestMapping("success")
    @ResponseBody
    public ResultSet success(String userId, String nextUserId)
    {
        Task task = taskService.createTaskQuery().taskAssignee(userId).singleResult();
        if (task == null)
        {
            return ResultSet.fail("任务不存在");
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("handler", userId);
        map.put("userId", nextUserId);
        map.put("approve", "yes");
        taskService.complete(task.getId(), map);
        Task task1 = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        if (task1 == null)
        {
            return ResultSet.fail("任务已完结");
        }
        //task1.setAssignee(nextUserId);
        return ResultSet.success("流程审核通过：" + task1.getId());
    }

    @RequestMapping("success/taskid")
    @ResponseBody
    public ResultSet success(String taskId, String userId, String nextUserId)
    {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null)
        {
            return ResultSet.fail("任务不存在");
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("handler", userId);
        map.put("userId", nextUserId);
        map.put("approve", "yes");
        taskService.complete(taskId, map);
        Task task1 = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        if (task1 == null)
        {
            return ResultSet.fail("任务已完结");
        }
        return ResultSet.success("流程审核通过：" + task1.getId());
    }

    @RequestMapping("success/level")
    @ResponseBody
    public ResultSet success(@NotNull String taskId, @NotNull String userId, @NotNull String nextUserId, String level)
    {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null)
        {
            return ResultSet.fail("任务不存在");
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("handler", userId);
        map.put("userId", nextUserId);
        map.put("approve", "yes");
        map.put("level", level);
        taskService.complete(taskId, map);
        ProcessInstanceQuery pi = runtimeService.createProcessInstanceQuery().processInstanceId(
            task.getProcessInstanceId());
        Task task1 = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        if (task1 == null)
        {
            return ResultSet.fail("任务已完结");
        }
        //task1.setAssignee(nextUserId);
        return ResultSet.success("流程审核通过：" + task1.getId());
    }

    @RequestMapping("reject")
    @ResponseBody
    public ResultSet reject(@NotNull String taskId, @NotNull String userId, @NotNull String nextUserId, String level)
    {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null)
        {
            return ResultSet.fail("任务不存在");
        }
        Task parentTask = taskService.createTaskQuery().taskId(task.getParentTaskId()).singleResult();
        HashMap<String, Object> map = new HashMap<>();
        map.put("handler", userId);
        //map.put("userId", parentTask.getAssignee());
        map.put("approve", "no");
        map.put("level", level);
        taskService.complete(taskId, map);
        Task task1 = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        if (task1 == null)
        {
            return ResultSet.fail("任务已完结");
        }
        System.out.println("驳回到上一节点处理人：" + task1.getAssignee());
        return ResultSet.success("流程驳回：task=" + task1.getId());
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultSet delete(@NotNull String taskId, String reason)
    {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null)
        {
            return ResultSet.fail("任务不存在");
        }
        runtimeService.deleteProcessInstance(taskId, reason);
        return ResultSet.success("删除成功:processId=" + task.getProcessInstanceId());
    }

    @RequestMapping("getTasks")
    @ResponseBody
    public ResultSet getTasks(@NotNull String userId)
    {
        List<Task> list = taskService.createTaskQuery().taskAssignee(userId).orderByTaskCreateTime().desc().list();
        String taskIds = "";
        for (Task task : list)
        {
            taskIds += task.getId() + ";";
        }
        return ResultSet.success(taskIds);
    }

    //@RequestMapping("rollback")
    //@ResponseBody
    public void rollback(String currentTaskId, String targetTaskId)
    {
        Task currentTask = taskService.createTaskQuery().taskId(currentTaskId).singleResult();
        if (currentTask == null)
        {
            throw new IllegalArgumentException("任务不存在");
        }
        List<String> currentTaskKeys = new ArrayList<>();
        currentTaskKeys.add(currentTask.getTaskDefinitionKey());
        runtimeService.createChangeActivityStateBuilder().processInstanceId(currentTask.getProcessInstanceId())
            .moveActivityIdsToSingleActivityId(currentTaskKeys, targetTaskId);

    }

    @RequestMapping("processDiagram")
    @ResponseBody
    public void createProcessDiagramPic(HttpServletResponse httpServletResponse, String taskId)
    {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(taskId).singleResult();

        //流程走完的不显示图
        if (pi == null)
        {
            return;
        }
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        String InstanceId = task.getProcessInstanceId();
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(InstanceId).list();

        //得到正在执行的Activity的Id
        List<String> activityIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();
        for (Execution exe : executions)
        {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
            activityIds.addAll(ids);
        }

        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
        ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engconf.getProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", activityIds, flows,
            engconf.getActivityFontName(), engconf.getLabelFontName(), engconf.getAnnotationFontName(),
            engconf.getClassLoader(), 1.0);
        OutputStream out = null;
        byte[] buf = new byte[1024];
        int legth = 0;
        try
        {
            out = httpServletResponse.getOutputStream();
            while ((legth = in.read(buf)) != -1)
            {
                out.write(buf, 0, legth);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
                if (out != null)
                {
                    out.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
