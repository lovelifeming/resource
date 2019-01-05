package com.zsm.sb.sensitive;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.lang.reflect.Field;


/**
 * 重置ToString(); 脱敏脱密
 *
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2018/8/28.
 * @Modified By:
 */
public class SensitiveToString extends ReflectionToStringBuilder
{
    private boolean maskFlag = false;

    public SensitiveToString(Object object, ToStringStyle style, boolean maskFlag)
    {
        super(object, style);
        this.maskFlag = maskFlag;
    }

    @Override
    protected Object getValue(Field field)
        throws IllegalAccessException
    {
        if (field.getType() == String.class && field.isAnnotationPresent(SensitiveInfo.class) && maskFlag)
        {
            String value = (String)field.get(this.getObject());
            switch (field.getAnnotation(SensitiveInfo.class).value())
            {
                case ID_CARD:
                    return SensitiveInfoUtils.idCard(value);
                case ADDRESS:
                    return SensitiveInfoUtils.address(value, 6);
                case MOBILE_PHONE:
                    return SensitiveInfoUtils.mobilePhone(value);
                case EMAIL:
                    return SensitiveInfoUtils.email(value);
                default:
                    return value;
            }
        }
        return field.get(this.getObject());
    }
}
