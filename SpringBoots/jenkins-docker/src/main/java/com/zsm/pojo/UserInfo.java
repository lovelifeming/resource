package com.zsm.pojo;

import lombok.Data;


/**
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2020-04-10 09:14.
 * @Modified By:
 */
@Data
public class UserInfo
{
    private String uuid;

    private String username;

    private String gender;

    private Data birthday;

    private String address;

    private String hobby;
}
