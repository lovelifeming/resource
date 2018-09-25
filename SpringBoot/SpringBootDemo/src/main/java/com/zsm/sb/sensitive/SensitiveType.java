package com.zsm.sb.sensitive;

/**
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2018/9/20.
 * @Modified By:
 */
public enum SensitiveType
{
    /**
     * 密码
     */
    PASSWORD,
    /**
     * 证件号
     */
    ID_CARD,
    /**
     * 中文名
     */
    CHINESE_NAME,

    /**
     * 座机号
     */
    FIXED_PHONE,
    /**
     * 手机号
     */
    MOBILE_PHONE,
    /**
     * 地址
     */
    ADDRESS,
    /**
     * 电子邮件
     */
    EMAIL,
    /**
     * 银行卡
     */
    BANK_CARD,
    /**
     * 公司开户银行联号
     */
    CNAPS_CODE,

    /**
     * 不做脱敏
     */
    NO_MASK
}
