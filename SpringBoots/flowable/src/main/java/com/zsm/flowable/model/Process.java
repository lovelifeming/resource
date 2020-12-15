package com.zsm.flowable.model;

import lombok.Data;

import java.util.Map;


/**
 * @Author: zeng.
 * @Date:Created in 2020-12-14 10:23.
 * @Description:
 */
@Data
public class Process
{
    /**
     * 工作流程Id
     */
    private String processId;

    /**
     * 工作流程内部默认Id，uuid
     */
    private String processDefinitionId;

    /**
     * 消息
     */
    private String message;

    /**
     * 自定义业务类型
     */
    private String businessKey;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 变量Map集合
     */
    private Map<String, Object> variables;
}
