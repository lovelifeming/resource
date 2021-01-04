package com.zsm.flowable.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * @Author: zeng.
 * @Date:Created in 2020-12-17 10:08.
 * @Description:
 */
@ApiModel("执行同意流程")
@Data
public class SuccessVO
{
    @ApiModelProperty("当前执行用户Id")
    @NotNull(message = "userId 不能为空")
    @NotEmpty(message = "userId 不能为空")
    private String userId;

    @ApiModelProperty("下一步执行用户Id")
    private String nextUserId;

    @ApiModelProperty("流程任务id")
    @NotNull(message = "taskId 不能为空")
    @NotEmpty(message = "taskId 不能为空")
    private String taskId;
}
