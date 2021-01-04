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
@ApiModel("开始流程")
@Data
public class StartVO
{
    @ApiModelProperty("当前业务流程key")
    @NotNull(message = "businessKey 不能为空")
    @NotEmpty(message = "businessKey 不能为空")
    private String businessKey;

    @ApiModelProperty("当前执行用户Id")
    @NotNull(message = "userId 不能为空")
    @NotEmpty(message = "userId 不能为空")
    private String userId;

    @ApiModelProperty("下一步执行用户Id")
    private String nextUserId;

    @ApiModelProperty("审批标识：yes  no")
    @NotNull(message = "approve 不能为空")
    @NotEmpty(message = "approve 不能为空")
    private String approve;

    @ApiModelProperty("确认赔偿人员：yes  no")
    private String verify;
}
