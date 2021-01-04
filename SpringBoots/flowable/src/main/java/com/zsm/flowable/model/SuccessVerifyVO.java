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
@ApiModel("判定赔偿人员执行同意流程")
@Data
public class SuccessVerifyVO
{
    @ApiModelProperty("当前执行用户Id")
    @NotNull(message = "userId 不能为空")
    @NotEmpty(message = "userId 不能为空")
    private String userId;

    @ApiModelProperty("下一步执行用户Id")
    @NotNull(message = "nextUserId 不能为空")
    @NotEmpty(message = "nextUserId 不能为空")
    private String nextUserId;

    @ApiModelProperty("流程任务id")
    @NotNull(message = "userId 不能为空")
    @NotEmpty(message = "userId 不能为空")
    private String taskId;

    @ApiModelProperty("确认赔偿人员：yes  no")
    private String verify;
}
