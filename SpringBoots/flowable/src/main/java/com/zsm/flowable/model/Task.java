package com.zsm.flowable.model;

import lombok.Data;


/**
 * @Author: zeng.
 * @Date:Created in 2020-12-14 10:26.
 * @Description:
 */
@Data
public class Task
{
    /**
     * 任务执行taskId
     */
    private String taskId;

    /**
     * 承受人
     */
    private String assignee;

    /**
     * 名称
     */
    private String name;

    /**
     * 拥有者
     */
    private String owner;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 描述
     */
    private String description;

    /**
     * 父级task
     */
    private String parentTask;

    /**
     * 优先级
     */
    private String priority;

    /**
     * 流程定义
     */
    private String processDefinition;

    /**
     * 流程实例
     */
    private String processInstance;

    /**
     * 任务默认键值key
     */
    private String taskDefinitionKey;

    /**
     * 承租人Id
     */
    private String tenantId;
}
