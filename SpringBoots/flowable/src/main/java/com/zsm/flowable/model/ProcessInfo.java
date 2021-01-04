package com.zsm.flowable.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * @Author: zeng.
 * @Date:Created in 2020-12-22 16:23.
 * @Description:
 */
@ApiModel("返回流程信息")
@Data
@AllArgsConstructor()
public class ProcessInfo
{
    @ApiModelProperty("流程任务id")
    private String taskId;

    @ApiModelProperty("流程任务id")
    private String processId;

    @ApiModelProperty("当前业务流程key")
    private String businessKey;
}
