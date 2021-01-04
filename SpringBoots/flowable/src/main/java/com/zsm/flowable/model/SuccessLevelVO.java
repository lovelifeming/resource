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
@ApiModel("判定级别执行同意流程")
@Data
public class SuccessLevelVO extends SuccessVerifyVO
{
    @ApiModelProperty("赔偿级别A/B/C/D")
    @NotNull(message = "level 不能为空")
    @NotEmpty(message = "level 不能为空")
    private String level;
}
