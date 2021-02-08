package com.zsm.flowable.controller;

import com.zsm.flowable.model.*;
import com.zsm.flowable.service.FlowableService;
import com.zsm.flowable.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.common.impl.identity.Authentication;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: zeng.
 * @Date:Created in 2020-12-08 15:38.
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/approve/")
@Api(description = "财物损失赔偿")
public class ApproveProcessController
{
    private static final String PROCESS_NAME = "approveProcess";

    private static final List<String> PROCESS_NODE = new ArrayList<String>()
    {
        {add("职员");}

        {add("部门经理");}

        {add("财务总监");}

        {add("会计审计");}

        {add("副总经理");}
    };

    @Autowired
    private FlowableService service;

    @ApiOperation("开始流程(创建并执行流程)")
    @PostMapping("start")
    public ResultSet startProcess(@RequestBody @Valid StartVO startVO, BindingResult results)
    {
        if (results.hasErrors())
            return ResultSet.fail(results.getFieldError().getDefaultMessage());
        HashMap<String, Object> processVariable = new HashMap<>();
        processVariable.put("handler", startVO.getUserId());
        processVariable.put("userId", startVO.getUserId());
        processVariable.put("approve", startVO.getApprove());
        processVariable.put("businessKey", startVO.getBusinessKey());
        Authentication.setAuthenticatedUserId(startVO.getUserId());
        String processId = service.startProcessInstanceByKey(PROCESS_NAME, startVO.getBusinessKey(),
            processVariable).getId();
        Authentication.setAuthenticatedUserId(null);
        log.info("startProcess创建流程 {}，userId={}，nextUserId={}，approve={},verify={},businessKey={}", PROCESS_NAME,
            startVO.getUserId(), startVO.getNextUserId(), startVO.getApprove(), startVO.getVerify(),
            startVO.getBusinessKey());
        service.setProcessVariable(processId, "verify", startVO.getVerify());
        String taskId = service.getOneTaskIdByProcessId(processId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("handler", startVO.getNextUserId());
        map.put("userId", startVO.getNextUserId());
        map.put("approve", startVO.getApprove());
        map.put("due", "no");
        service.complete(taskId, map);
        log.info("startProcess成功执行流程，processId={},taskId={},businessKey={}", processId, taskId,
            startVO.getBusinessKey());
        Task task = service.getOneTaskByProcessId(processId);
        if (task == null)
        {
            return ResultSet.success("任务已完成");
        }
        ProcessInfo info = new ProcessInfo(task.getId(), task.getProcessInstanceId(), startVO.getBusinessKey());
        return ResultSet.success(info);
    }

    @ApiOperation("创建流程")
    @PostMapping("create")
    public ResultSet create(@RequestParam @ApiParam("当前业务流程key") String businessKey,
                            @RequestParam @ApiParam("下一步执行用户Id") String userId)
    {
        if (StringUtils.isBlank(businessKey) || StringUtils.isBlank(userId))
        {
            return ResultSet.fail("businessKey 或 userId 参数为空");
        }
        HashMap<String, Object> processVariable = new HashMap<>();
        processVariable.put("handler", userId);
        processVariable.put("userId", userId);
        processVariable.put("businessKey", businessKey);
        ProcessInstance instance = service.startProcessInstanceByKey(PROCESS_NAME, businessKey, processVariable);
        Task task = service.getOneTaskByProcessId(instance.getId());
        ProcessInfo info = new ProcessInfo(task.getId(), task.getProcessInstanceId(), instance.getBusinessKey());
        return ResultSet.success(info);
    }

    @ApiOperation("执行同意流程")
    @PostMapping("success")
    public ResultSet success(@RequestBody @Valid SuccessVO successVO, BindingResult results)
    {
        if (results.hasErrors())
            return ResultSet.fail(results.getFieldError().getDefaultMessage());
        Task task = service.getOneTaskByTaskId(successVO.getTaskId());
        if (task == null)
        {
            return ResultSet.fail("任务不存在");
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("handler", successVO.getUserId());
        map.put("userId", successVO.getNextUserId());
        map.put("approve", "yes");
        map.put("due", "no");
        if ("财务总监".equals(task.getName()))
        {
            Object level = service.getProcessVariable(task.getProcessInstanceId(), "level");
            map.put("level", level);
        }
        else if ("会计审计".equals(task.getName()))
        {
            Object verify = service.getProcessVariable(task.getProcessInstanceId(), "verify");
            map.put("verify", verify);
        }
        ProcessInstance instance = service.getProcessByProcessId(task.getProcessInstanceId());
        service.complete(successVO.getTaskId(), map);
        log.info("success成功执行流程，userId={},nextUserId={},processId={},taskId={},businessKey={}", successVO.getUserId(),
            successVO.getNextUserId(), instance.getId(), successVO.getTaskId(), instance.getBusinessKey());
        Task task1 = service.getOneTaskByProcessId(task.getProcessInstanceId());
        if (task1 == null)
        {
            return ResultSet.success("流程已完结");
        }
        ProcessInfo info = new ProcessInfo(task1.getId(), task1.getProcessInstanceId(), instance.getBusinessKey());
        return ResultSet.success(info);
    }

    @ApiOperation("执行同意流程（部门经理）")
    @PostMapping("successLevel")
    public ResultSet successLevel(@RequestBody @Valid SuccessLevelVO levelVO, BindingResult results)
    {
        if (results.hasErrors())
            return ResultSet.fail(results.getFieldError().getDefaultMessage());
        Task task = service.getOneTaskByTaskId(levelVO.getTaskId());
        if (task == null)
        {
            return ResultSet.fail("任务不存在");
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("handler", levelVO.getUserId());
        map.put("userId", levelVO.getNextUserId());
        map.put("approve", "yes");
        map.put("level", levelVO.getLevel());
        map.put("due", "no");
        service.complete(levelVO.getTaskId(), map);

        ProcessInstance instance = service.getProcessByProcessId(task.getProcessInstanceId());
        service.setProcessVariable(instance.getId(), "level", levelVO.getLevel());
        log.info("successLevel成功执行流程，userId={},nextUserId={},level={},processId={},taskId={},businessKey={}",
            levelVO.getUserId(), levelVO.getNextUserId(), levelVO.getLevel(), instance.getId(), levelVO.getTaskId(),
            instance.getBusinessKey());
        if (StringUtils.isNotBlank(levelVO.getVerify()) && "yes".equals(levelVO.getVerify()))
        {
            service.setProcessVariable(instance.getId(), "verify", levelVO.getVerify());
        }
        Task task1 = service.getOneTaskByProcessId(task.getProcessInstanceId());
        if (task1 == null)
        {
            return ResultSet.success("流程已完结");
        }
        ProcessInfo info = new ProcessInfo(task1.getId(), task1.getProcessInstanceId(), instance.getBusinessKey());
        return ResultSet.success(info);
    }

    @ApiOperation("执行同意流程（职员）")
    @PostMapping("successVerify")
    public ResultSet successVerify(@RequestBody @Valid SuccessVerifyVO levelVO, BindingResult results)
    {
        if (results.hasErrors())
            return ResultSet.fail(results.getFieldError().getDefaultMessage());
        Task task = service.getOneTaskByTaskId(levelVO.getTaskId());
        if (task == null)
        {
            return ResultSet.fail("任务不存在");
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("handler", levelVO.getUserId());
        map.put("userId", levelVO.getNextUserId());
        map.put("approve", "yes");
        map.put("due", "no");
        service.complete(levelVO.getTaskId(), map);

        ProcessInstance instance = service.getProcessByProcessId(task.getProcessInstanceId());
        if (StringUtils.isNotBlank(levelVO.getVerify()))
        {
            service.setProcessVariable(instance.getId(), "verify", levelVO.getVerify());
        }
        Task task1 = service.getOneTaskByProcessId(task.getProcessInstanceId());
        if (task1 == null)
        {
            return ResultSet.success("流程已完结");
        }
        ProcessInfo info = new ProcessInfo(task1.getId(), task1.getProcessInstanceId(), instance.getBusinessKey());
        return ResultSet.success(info);
    }

    @ApiOperation("驳回流程")
    @PostMapping("reject")
    public ResultSet reject(@RequestParam @ApiParam("任务流程Id") String taskId,
                            @RequestParam @ApiParam("用户Id") String userId)
    {
        if (StringUtils.isBlank(taskId))
            return ResultSet.fail("taskId不能为空");
        Task task = service.getOneTaskByTaskId(taskId);
        if (task == null)
        {
            return ResultSet.fail("任务不存在");
        }
        int index = PROCESS_NODE.indexOf(task.getName()) - 1;
        if (index < 0)
        {
            return ResultSet.fail("无法驳回");
        }
        String nodeName = PROCESS_NODE.get(index);
        ProcessInstance instance = service.getProcessByProcessId(task.getProcessInstanceId());
        List<HistoricTaskInstance> list = service.getHistoricTaskInstances(instance.getId(), nodeName);
        HashMap<String, Object> map = new HashMap<>();
        map.put("handler", userId);
        map.put("approve", "no");
        map.put("userId", list.get(0).getAssignee());
        map.put("due", "no");
        service.complete(taskId, map);
        log.info("reject成功执行流程，userId={},nextUserId={},processId={},taskId={},businessKey={}",
            userId, list.get(0).getAssignee(), instance.getId(), taskId, instance.getBusinessKey());
        Task task1 = service.getOneTaskByProcessId(task.getProcessInstanceId());
        if (task1 == null)
        {
            return ResultSet.success("流程已完结");
        }
        log.info("驳回流程到上一级节点处理人：" + task1.getAssignee());
        ProcessInfo info = new ProcessInfo(task1.getId(), task1.getProcessInstanceId(), instance.getBusinessKey());
        return ResultSet.success(info);
    }

    @ApiOperation("过期流程")
    @PostMapping("pastDue")
    public ResultSet pastDue(@RequestBody @Valid SuccessVO successVO, BindingResult results)
    {
        if (results.hasErrors())
            return ResultSet.fail(results.getFieldError().getDefaultMessage());
        Task task = service.getOneTaskByTaskId(successVO.getTaskId());
        if (task == null)
        {
            return ResultSet.fail("任务不存在");
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("handler", successVO.getUserId());
        map.put("userId", successVO.getNextUserId());
        map.put("due", "yes");
        ProcessInstance instance = service.getProcessByProcessId(task.getProcessInstanceId());
        service.complete(successVO.getTaskId(), map);
        log.info("pastDue成功执行流程，userId={},nextUserId={},processId={},taskId={},businessKey={}",
            successVO.getUserId(), successVO.getNextUserId(), instance.getId(), successVO.getTaskId(),
            instance.getBusinessKey());
        Task task1 = service.getOneTaskByProcessId(task.getProcessInstanceId());
        ProcessInfo info = new ProcessInfo(task1.getId(), task1.getProcessInstanceId(), instance.getBusinessKey());
        return ResultSet.success(info);
    }

    @ApiOperation("会计审计结束流程")
    @GetMapping("finishTask")
    public ResultSet finishTask(@RequestParam @ApiParam("任务流程Id") String taskId,
                                @RequestParam(required = false) @ApiParam("用户Id") String userId)
    {
        if (StringUtils.isBlank(taskId))
            return ResultSet.fail("taskId不能为空");
        Task task = service.getOneTaskByTaskId(taskId);
        if (task == null)
        {
            return ResultSet.fail("任务不存在");
        }
        Object businessKey = service.getProcessVariable(task.getProcessInstanceId(), "businessKey");
        HashMap<String, Object> map = new HashMap<>();
        map.put("due", "yes");
        service.complete(taskId, map);
        log.info("finishTask成功执行流程，userId={},processId={},taskId={},businessKey={}",
            userId, task.getProcessInstanceId(), taskId, businessKey);
        return ResultSet.success("流程已完结");
    }

    @ApiOperation("删除流程")
    @GetMapping("delete")
    public ResultSet delete(@RequestParam @ApiParam("任务流程Id") String taskId,
                            @RequestParam(required = false) @ApiParam("用户Id") String userId,
                            @RequestParam(required = false) @ApiParam("删除原因") String reason)
    {
        if (StringUtils.isBlank(taskId))
            return ResultSet.fail("taskId不能为空");
        Task task = service.getOneTaskByTaskId(taskId);
        if (task == null)
        {
            return ResultSet.fail("任务不存在");
        }
        ProcessInstance instance = service.getProcessByProcessId(task.getProcessInstanceId());
        service.deleteProcessInstance(task.getProcessInstanceId(), reason);

        log.info("delete成功执行流程，userId={},processId={},taskId={},businessKey={}",
            userId, task.getProcessInstanceId(), taskId, instance.getBusinessKey());
        ProcessInfo info = new ProcessInfo(task.getId(), task.getProcessInstanceId(), instance.getBusinessKey());
        return ResultSet.success(info);
    }

    @ApiOperation("查询流程")
    @GetMapping("getTasks")
    public ResultSet getTasks(@RequestParam @ApiParam("用户Id") String userId)
    {
        if (StringUtils.isBlank(userId))
            return ResultSet.fail("userId 不能为空");
        List<Task> list = service.getTasksByUserId(userId);
        List<Map<String, Object>> taskIds = new ArrayList<>();
        for (Task task : list)
        {
            String businessKey = service.getHistoricProcessInstance(task.getProcessInstanceId()).getBusinessKey();
            buildTaskIdBusinessKey(taskIds, businessKey, task.getId());
        }
        return ResultSet.success(taskIds);
    }

    @ApiOperation("查询处理过的流程")
    @GetMapping("getTasked")
    public ResultSet getTasked(@RequestParam @ApiParam("用户Id") String userId)
    {
        if (StringUtils.isBlank(userId))
            return ResultSet.fail("userId 不能为空");
        List<HistoricTaskInstance> list = service.getHistoricTaskInstancesByTaskAssignee(userId);
        List<Map<String, Object>> taskIds = new ArrayList<>();
        for (HistoricTaskInstance task : list)
        {
            String businessKey = service.getHistoricProcessInstance(task.getProcessInstanceId()).getBusinessKey();
            buildTaskIdBusinessKey(taskIds, businessKey, task.getId());
        }
        return ResultSet.success(taskIds);
    }

    private void buildTaskIdBusinessKey(List<Map<String, Object>> taskIds, String businessKey, Object taskId)
    {
        Map<String, Object> tmp = new HashMap<String, Object>();
        tmp.put("taskId", taskId);
        tmp.put("businessKey", businessKey);
        taskIds.add(tmp);
    }

    @ApiOperation("根据业务key查询流程")
    @GetMapping("getTaskByBusinessKey")
    public ResultSet getTaskByBusinessKey(@RequestParam @ApiParam("业务key") String businessKey)
    {
        if (StringUtils.isBlank(businessKey))
            return ResultSet.fail("businessKey 不能为空");
        List<Task> list = service.getTasksByBusinessKey(businessKey);
        if (list.size() <= 0)
            return ResultSet.fail("任务不存在");
        List<Map<String, Object>> taskIds = new ArrayList<>();
        Map<String, Object> tmp = new HashMap<String, Object>();
        tmp.put("taskId", list.get(0).getId());
        taskIds.add(tmp);
        return ResultSet.success(taskIds);
    }

    @ApiOperation("查询流程")
    @GetMapping("getTaskDetail")
    public ResultSet getTaskDetail(@RequestParam @ApiParam("任务流程Id") String taskId)
    {
        if (StringUtils.isBlank(taskId))
            return ResultSet.fail("taskId 不能为空");
        HistoricTaskInstance ht = service.getHistoricTaskInstance(taskId);
        if (ht == null)
            return ResultSet.success(new ArrayList<TaskDetail>());
        List<HistoricTaskInstance> list = service.getHistoricTaskInstances(ht.getProcessInstanceId());
        List<TaskDetail> res = buildTaskDetails(list);
        return ResultSet.success(res);
    }

    @ApiOperation("查询流程(通过业务key)")
    @GetMapping("getTaskDetailByBusinessKey")
    public ResultSet getTaskDetailByBusinessKey(@RequestParam @ApiParam("业务key") String businessKey)
    {
        if (StringUtils.isBlank(businessKey))
            return ResultSet.fail("businessKey 不能为空");
        List<HistoricTaskInstance> list = service.getHistoricTaskInstancesByBusinessKey(businessKey);
        List<TaskDetail> res = buildTaskDetails(list);
        return ResultSet.success(res);
    }

    private List<TaskDetail> buildTaskDetails(List<HistoricTaskInstance> list)
    {
        List<TaskDetail> res = new ArrayList<>();
        for (HistoricTaskInstance n : list)
        {
            TaskDetail tmp = new TaskDetail();
            tmp.setUserId(n.getAssignee());
            tmp.setTaskName(n.getName());
            tmp.setStartTime(CommonUtil.convertDate(n.getStartTime()));
            tmp.setEndTime(CommonUtil.convertDate(n.getEndTime()));
            tmp.setTaskId(n.getId());
            tmp.setProcessId(n.getProcessInstanceId());
            res.add(tmp);
        }
        return res;
    }

    @ApiOperation("查询流程")
    @GetMapping("getProcessDetail")
    public ResultSet getProcessDetail(@RequestParam(required = false) @ApiParam("流程名称") String processName,
                                      @RequestParam(required = false) @ApiParam("流程id") String processId,
                                      @RequestParam(required = false) @ApiParam("开始时间") String startTime,
                                      @RequestParam(required = false) @ApiParam("结束时间") String endTime,
                                      @RequestParam(required = false) @ApiParam("当前处理人") String assignee,
                                      @RequestParam(required = false) @ApiParam("状态：1是未完成，2是已完成") String rev,
                                      @RequestParam @ApiParam("页数") Integer pageNum,
                                      @RequestParam @ApiParam("每页记录条数") Integer pageSize)
    {
        return service.queryManageInfo(processName, processId, startTime, endTime, assignee, rev, pageNum,
            pageSize);
    }

    @ApiOperation("回退流程")
    @GetMapping("rollback")
    @ResponseBody
    public void rollback(String currentTaskId, String targetTaskId)
    {
        if (StringUtils.isBlank(currentTaskId) || StringUtils.isBlank(targetTaskId))
            throw new IllegalArgumentException("userId 不能为空");
        Task currentTask = service.getOneTaskByTaskId(currentTaskId);
        if (currentTask == null)
        {
            throw new IllegalArgumentException("任务不存在");
        }
        List<String> currentTaskKeys = new ArrayList<>();
        currentTaskKeys.add(currentTask.getTaskDefinitionKey());
        service.changeActivityState(currentTask.getProcessInstanceId(), targetTaskId, currentTaskKeys);
    }

    @ApiOperation("获取流程图")
    @GetMapping("getProcessDiagram")
    public void createProcessDiagram(HttpServletResponse response, @RequestParam @ApiParam("流程Id") String processId)
        throws IOException
    {
        if (StringUtils.isBlank(processId))
            response.getWriter().write("processId 不能为空");
        ProcessInstance pi = service.getProcessByProcessId(processId);
        //流程走完的不显示图
        if (pi == null)
        {
            response.getWriter().write("任务已完成");
            return;
        }
        Task task = service.getOneTaskByProcessId(pi.getId());
        //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        String InstanceId = task.getProcessInstanceId();
        List<Execution> executions = service.getExecutions(InstanceId);

        //得到正在执行的Activity的Id
        List<String> activityIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();
        for (Execution exe : executions)
        {
            List<String> ids = service.getActiveActivityIds(exe.getId());
            activityIds.addAll(ids);
        }

        //获取流程图
        BpmnModel bpmnModel = service.getBpmnModel(pi.getProcessDefinitionId());
        ProcessEngineConfiguration engconf = service.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engconf.getProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", activityIds, flows,
            engconf.getActivityFontName(), engconf.getLabelFontName(), engconf.getAnnotationFontName(),
            engconf.getClassLoader(), 1.0);
        // 自定义画图类
        //InputStream in = new MyProcessDiagramGenerator().generateDiagram(bpmnModel, "png", activityIds, flows);
        OutputStream out = null;
        byte[] buf = new byte[1024];
        int legth = 0;
        try
        {
            out = response.getOutputStream();
            while ((legth = in.read(buf)) != -1)
            {
                out.write(buf, 0, legth);
            }
        }
        finally
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
    }

}
