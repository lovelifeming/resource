package com.zsm.sb.sensitive;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Objects;


/**
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2018/9/20.
 * @Modified By:
 */
public class SensitiveSerialize extends JsonSerializer<String> implements ContextualSerializer
{
    private SensitiveType type;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider provider)
        throws IOException
    {
        switch (this.type)
        {
            case CHINESE_NAME:
            {
                gen.writeString(SensitiveInfoUtils.chineseName(value));
                break;
            }
            case ID_CARD:
            {
                gen.writeString(SensitiveInfoUtils.idCard(value));
                break;
            }
            case FIXED_PHONE:
            {
                gen.writeString(SensitiveInfoUtils.fixedPhone(value));
                break;
            }
            case MOBILE_PHONE:
            {
                gen.writeString(SensitiveInfoUtils.mobilePhone(value));
                break;
            }
            case ADDRESS:
            {
                gen.writeString(SensitiveInfoUtils.address(value, 4));
                break;
            }
            case EMAIL:
            {
                gen.writeString(SensitiveInfoUtils.email(value));
                break;
            }
            case BANK_CARD:
            {
                gen.writeString(SensitiveInfoUtils.bankCard(value));
                break;
            }
            case CNAPS_CODE:
            {
                gen.writeString(SensitiveInfoUtils.cnapsCode(value));
                break;
            }
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
        throws JsonMappingException
    {
        if (property != null)
        {   // 为空直接跳过
            if (Objects.equals(property.getType().getRawClass(), String.class))
            { // 非 String 类直接跳过
                SensitiveInfo sensitiveInfo = property.getAnnotation(SensitiveInfo.class);
                if (sensitiveInfo == null)
                {
                    sensitiveInfo = property.getContextAnnotation(SensitiveInfo.class);
                }
                if (sensitiveInfo != null)
                {   // 如果能得到注解，就将注解的 value 传入 SensitiveInfoSerialize
                    SensitiveSerialize serialize = new SensitiveSerialize();
                    serialize.type = sensitiveInfo.value();
                    return serialize;
                }
            }
            return prov.findValueSerializer(property.getType(), property);
        }
        return prov.findNullValueSerializer(property);
    }
}
