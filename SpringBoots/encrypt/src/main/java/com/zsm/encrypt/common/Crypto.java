package com.zsm.encrypt.common;

/**
 * @Author: zengsm.
 * @Date:Created in 2021-04-06 22:20.
 * @Description:
 */


import cn.hutool.crypto.CryptoException;
import com.zsm.encrypt.util.CharsetUtil;
import com.zsm.encrypt.util.IoUtil;
import com.zsm.encrypt.util.RadixUtil;
import org.springframework.util.FastByteArrayOutputStream;

import java.io.InputStream;
import java.io.OutputStream;


/**
 * 加密解密接口，对于MD5，SHA1等摘要算法解密方法直接抛出异常.
 * 如果遇到结果或者入参是byte字节数组类型的，默认就转换为16进制的字符串，为了好统一使用String来表达
 * 加密解密其实更好的是针对byte[]其变种就是inputStream和outputStream，字符串类型的只是一层封装
 */
public interface Crypto
{
    /**
     * 加密
     *
     * @param src 待加密字节数组
     * @return 加密后的
     */
    byte[] encrypt(byte[] src);

    /**
     * 解密
     *
     * @param src 待解密字节数组
     * @return 加密后的
     */
    byte[] decrypt(byte[] src);

    /**
     * 加密，如果数据量不大，才使用该接口默认的方式，因为它使用了缓冲数组，如果数据量大请自行实现
     *
     * @param in  输入流
     * @param out 输出流
     */
    default void encrypt(InputStream in, OutputStream out)
    {
        try
        {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FastByteArrayOutputStream baos = new FastByteArrayOutputStream();
            IoUtil.copy(in, baos);
            byte[] bytes = baos.toByteArray();
            byte[] encrypted = encrypt(bytes);
            out.write(encrypted);
        }
        catch (Exception e)
        {
            throw new CryptoException(e);
        }
    }

    /**
     * 解密，如果数据量不大，才使用该接口默认的方式，因为它使用了缓冲数组，如果数据量大请自行实现
     *
     * @param in  输入流
     * @param out 输出流
     */
    default void decrypt(InputStream in, OutputStream out)
    {
        try
        {
            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FastByteArrayOutputStream baos = new FastByteArrayOutputStream();
            IoUtil.copy(in, baos);
            byte[] bytes = baos.toByteArray();
            byte[] encrypted = decrypt(bytes);
            out.write(encrypted);
        }
        catch (Exception e)
        {
            throw new CryptoException(e);
        }
    }

    /**
     * 加密
     *
     * @param src     原字符串
     * @param charset 字符编码
     * @return 加密后的
     */
    default String encrypt(String src, String charset)
    {
        byte[] bytes = src.getBytes(CharsetUtil.charset(charset));
        byte[] encrypted = encrypt(bytes);
        return RadixUtil.toHex(encrypted);
    }

    /**
     * 加密
     *
     * @param src 原字符串
     * @return 加密后的
     */
    default String encrypt(String src)
    {
        return encrypt(src, CharsetUtil.CHARSET_UTF_8.name());
    }

    /**
     * 解密
     *
     * @param src     原字符串
     * @param charset 返回的字符串编码
     * @return 解密后的
     */
    default String decrypt(String src, String charset)
    {
        byte[] bytes = RadixUtil.toBytes(src);
        byte[] decrypted = decrypt(bytes);
        return new String(decrypted, CharsetUtil.charset(charset));
    }

    /**
     * 解密
     *
     * @param src 原字符串
     * @return 解密后的
     */
    default String decrypt(String src)
    {
        return decrypt(src, CharsetUtil.UTF_8);
    }
}
