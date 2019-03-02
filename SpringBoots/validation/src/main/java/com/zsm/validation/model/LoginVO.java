package com.zsm.validation.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2019/2/25 17:20.
 * @Modified By:
 */
@Data
public class LoginVO
{
    @NotBlank(message = "用户名不能为空")
    @Email
    private String username;
    @NotBlank(message = "密码不能为空")
    @Length(min = 6,message = "密码长度至少6位")
    private String password;
}
