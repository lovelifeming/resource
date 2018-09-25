package com.zsm.sb.sensitive;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2018/9/20.
 * @Modified By:
 */
public class SensitiveInfoUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveInfoUtils.class);

    public static String password(String pwd)
    {
        if (StringUtils.isBlank(pwd))
        {
            return "******";
        }
        return StringUtils.repeat("*",pwd.length());
    }

    public static String address(String address, int sensitiveSize)
    {
        if (StringUtils.isBlank(address))
        {
            return "";
        }
        int length = StringUtils.length(address);
        int index = StringUtils.lastIndexOf(address, '-');
        if (index > 1 && index < length / 2 && index < sensitiveSize * 2)
        {
            return StringUtils.rightPad(StringUtils.left(address, length - index), length, "*");
        }
        if (length < sensitiveSize)
        {
            return StringUtils.rightPad(StringUtils.left(address, length / 2), length, "*");
        }
        return StringUtils.rightPad(StringUtils.left(address, length - sensitiveSize), length, "*");
    }

    public static String idCard(String id)
    {
        if (StringUtils.isBlank(id))
        {
            return "";
        }
        int length = StringUtils.length(id);
        if (length < 8)
        {
            return StringUtils.left(id, 4).concat(StringUtils.leftPad(StringUtils.right(id, 4), length, "*"));
        }
        String num = StringUtils.right(id, 4);
        return StringUtils.left(id, 4).concat(StringUtils.leftPad(num, StringUtils.length(id) - 4, "*"));
    }

    public static String mobilePhone(String num)
    {
        if (StringUtils.isBlank(num))
        {
            return "";
        }
        int length = StringUtils.length(num);
        if (length < 11)
        {
            //固定电话 后四位，其他隐藏  028****1234
            return StringUtils.leftPad(StringUtils.right(num, 4), StringUtils.length(num), "*");
        }
        return StringUtils.left(num, 3).concat(StringUtils.removeStart(
            StringUtils.leftPad(StringUtils.right(num, 4), StringUtils.length(num), "*"), "***"));
    }

    public static String email(String email)
    {
        if (StringUtils.isBlank(email))
        {
            return "";
        }
        int index = StringUtils.indexOf(email, '@');
        if (index < 1)
        {
            return email;
        }
        return StringUtils.rightPad(StringUtils.left(email, 2), index, "*")
            .concat(StringUtils.mid(email, index, StringUtils.length(email)));
    }

    public static String chineseName(String name)
    {
        if (StringUtils.isBlank(name) || name.length() < 2)
        {
            return name;
        }
        if (name.length() == 2)
        {
            return StringUtils.rightPad(StringUtils.left(name, 1), name.length(), "*");
        }
        return StringUtils.rightPad(StringUtils.left(name, 1), name.length() - 1, "*")
            .concat(StringUtils.right(name, 1));
    }

    public static String fixedPhone(String phone)
    {
        if (StringUtils.isBlank(phone))
        {
            return "";
        }
        return StringUtils.leftPad(StringUtils.right(phone, 4), StringUtils.length(phone), "*");
    }

    public static String bankCard(String bankCard)
    {
        if (StringUtils.isBlank(bankCard))
        {
            return "";
        }
        return StringUtils.left(bankCard, 6).concat(StringUtils.removeStart(
            StringUtils.leftPad(StringUtils.right(bankCard, 4),
                StringUtils.length(bankCard), "*"), "******"));
    }

    public static String cnapsCode(String cnaps)
    {
        if (StringUtils.isBlank(cnaps))
        {
            return "";
        }
        return StringUtils.rightPad(StringUtils.left(cnaps, 2), StringUtils.length(cnaps), "*");
    }
}
