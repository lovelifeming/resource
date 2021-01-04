package com.zsm.flowable.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Author: zeng.
 * @Date:Created in 2020-12-22 16:44.
 * @Description:
 */
@ApiModel("任务流程详情")
@Data
public class TaskDetail
{
    @ApiModelProperty("流程任务id")
    private String taskId;

    @ApiModelProperty("流程id")
    private String processId;

    @ApiModelProperty("业务key")
    private String businessKey;

    @ApiModelProperty("授权处理人id")
    private String userId;

    @ApiModelProperty("任务节点名称")
    private String taskName;

    @ApiModelProperty("流程任务id")
    private String startTime;

    @ApiModelProperty("流程任务id")
    private String endTime;

}
