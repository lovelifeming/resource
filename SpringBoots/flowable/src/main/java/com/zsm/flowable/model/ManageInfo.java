package com.zsm.flowable.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Author: zeng.
 * @Date:Created in 2021-01-07 10:28.
 * @Description:
 */
@Data
@ApiModel("流程管理信息")
public class ManageInfo
{
    @ApiModelProperty("流程id")
    private String processId;

    @ApiModelProperty("流程名称")
    private String processName;

    @ApiModelProperty("当前业务流程key")
    private String businessKey;

    @ApiModelProperty("流程开始时间")
    private String startTime;

    @ApiModelProperty("当前处理人")
    private String assignee;

    @ApiModelProperty("状态：1是未完成，2是已完成")
    private String rev;
}
