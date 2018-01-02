package com.zsm.sb.util;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2017/12/29 17:48.
 * @Modified By:
 */
public interface MyMapper<T> extends Mapper<T>,MySqlMapper<T>
{
}
