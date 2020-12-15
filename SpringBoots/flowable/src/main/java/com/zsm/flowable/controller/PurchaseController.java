package com.zsm.flowable.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: zeng.
 * @Date:Created in 2020-12-08 14:24.
 * @Description:
 */
@RestController
@RequestMapping("/purchase/")
public class PurchaseController
{
    //@Autowired
    //private RepositoryService repositoryService;
    //
    //@Autowired
    //private RuntimeService runtimeService;
    //
    //@Autowired
    //private TaskService taskService;
    //
    //@Autowired
    //private HistoryService historyService;
    //
    //@Autowired
    //private ProcessEngine processEngine;
    //
    ///**
    // * .提交采购订单的审批请求
    // *
    // * @param userId 用户id
    // */
    //@PostMapping("/start/{userId}/{purchaseOrderId}")
    //public ResultSet startFlow(@PathVariable String userId, @PathVariable String purchaseOrderId)
    //{
    //    HashMap<String, Object> map = new HashMap<>();
    //    map.put("userId", userId);
    //    map.put("purchaseOrderId", purchaseOrderId);
    //    ProcessInstance processInstance =
    //        runtimeService.startProcessInstanceByKey("OrderApproval", map);
    //    String processId = processInstance.getId();
    //    String name = processInstance.getName();
    //    System.out.println(processId + ":" + name);
    //    return ResultSet.success(processId + ":" + name);
    //}
    //
    ///**
    // * .获取用户的任务
    // *
    // * @param userId 用户id
    // */
    //@GetMapping("/getTasks/{userId}")
    //public ResultSet<String> getTasks(@PathVariable String userId)
    //{
    //    List<Task> tasks = taskService.createTaskQuery().taskAssignee(userId).orderByTaskCreateTime().desc().list();
    //    return ResultSet.success(tasks.toString());
    //}
    //
    ///**
    // * .审批通过
    // */
    //@PostMapping("/success/{taskId}")
    //public ResultSet success(@PathVariable String taskId)
    //{
    //    Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
    //    if (task == null)
    //    {
    //        return ResultSet.fail("流程不存在");
    //    }
    //    //通过审核
    //    HashMap<String, Object> map = new HashMap<>();
    //    map.put("approved", true);
    //    taskService.complete(taskId, map);
    //    return ResultSet.success("流程审核通过！");
    //}
    //
    ///**
    // * .审批不通过
    // */
    //@PostMapping("/faile/{taskId}")
    //public ResultSet faile(@PathVariable String taskId)
    //{
    //    Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
    //    if (task == null)
    //    {
    //        return ResultSet.fail("流程不存在");
    //    }
    //    //通过审核
    //    HashMap<String, Object> map = new HashMap<>();
    //    map.put("approved", false);
    //    taskService.complete(taskId, map);
    //    return ResultSet.success("审批驳回");
    //}
    //
    //@RequestMapping(value = "processDiagram")
    //public void genProcessDiagram(HttpServletResponse httpServletResponse, String processId)
    //    throws Exception
    //{
    //    ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
    //
    //    //流程走完的不显示图
    //    if (pi == null)
    //    {
    //        return;
    //    }
    //    Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
    //    //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
    //    String InstanceId = task.getProcessInstanceId();
    //    List<Execution> executions = runtimeService
    //        .createExecutionQuery()
    //        .processInstanceId(InstanceId)
    //        .list();
    //
    //    //得到正在执行的Activity的Id
    //    List<String> activityIds = new ArrayList<>();
    //    List<String> flows = new ArrayList<>();
    //    for (Execution exe : executions)
    //    {
    //        List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
    //        activityIds.addAll(ids);
    //    }
    //
    //    //获取流程图
    //    BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
    //    ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
    //    ProcessDiagramGenerator diagramGenerator = engconf.getProcessDiagramGenerator();
    //    InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", activityIds, flows,
    //        engconf.getActivityFontName(), engconf.getLabelFontName(), engconf.getAnnotationFontName(),
    //        engconf.getClassLoader(), 1.0);
    //    OutputStream out = null;
    //    byte[] buf = new byte[1024];
    //    int legth = 0;
    //    try
    //    {
    //        out = httpServletResponse.getOutputStream();
    //        while ((legth = in.read(buf)) != -1)
    //        {
    //            out.write(buf, 0, legth);
    //        }
    //    }
    //    finally
    //    {
    //        if (in != null)
    //        {
    //            in.close();
    //        }
    //        if (out != null)
    //        {
    //            out.close();
    //        }
    //    }
    //}
}
