package com.zsm.encrypt.util;

/**
 * @Author: zengsm.
 * @Date:Created in 2021-04-06 22:41.
 * @Description: 进制转换类
 */
public class RadixUtil
{
    private RadixUtil()
    {
    }

    /**
     * 字节数组转换为16进制字符串, 大写
     *
     * @param b 字节数字
     * @return 十六进制字符串 小写
     */
    public static String toHex(byte[] b)
    {
        return toHexLower(b).toUpperCase();
    }

    /**
     * 字节数组转换为16进制字符串, 小写
     *
     * @param b 字节数字
     * @return 十六进制字符串 小写
     */
    public static String toHexLower(byte[] b)
    {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++)
        {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
            {
                plainText = "0" + plainText;
            }
            hexString.append(plainText);
        }
        return hexString.toString();
    }

    /**
     * 16进制字符串转换为字节数组
     *
     * @param hex 十六进制字符串
     * @return 字节数组
     */
    public static byte[] toBytes(String hex)
    {
        byte[] digest = new byte[hex.length() / 2];
        for (int i = 0; i < digest.length; i++)
        {
            String byteString = hex.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte)byteValue;
        }
        return digest;
    }
}
