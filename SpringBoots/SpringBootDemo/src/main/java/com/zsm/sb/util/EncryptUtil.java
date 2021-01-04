package com.zsm.sb.util;

import org.apache.tomcat.util.codec.binary.Base64;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;


/**
 * @Author: zeng.
 * @Date:Created in 2020-12-16 14:40.
 * @Description:
 */
public class EncryptUtil
{
    private static final String AES = "AES";

    public static String base64Encode(byte[] bytes)
    {
        return Base64.encodeBase64String(bytes);
    }

    public static byte[] base64Decode(String base64Code)
        throws Exception
    {
        return new BASE64Decoder().decodeBuffer(base64Code);
    }

    public static byte[] aesEncryptToBytes(String content, String encryptKey)
        throws Exception
    {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));
        return cipher.doFinal(content.getBytes("UTF-8"));
    }

    public static String aesEncrypt(String content, String encryptKey)
        throws Exception
    {
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }

    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey)
        throws Exception
    {
        KeyGenerator kgen = KeyGenerator.getInstance(AES);
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), AES));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    public static String aesDecrypt(String encryptStr, String decryptKey)
        throws Exception
    {
        return aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
    }

    public static void main(String[] args)
        throws Exception
    {
        String key = "abc!@#$%^&*_+123";       //加密密钥为18个字符
        String content = "123456";

        System.out.println("加密/解密密钥：" + key);
        System.out.println("加密前：" + content);
        String encrypt = EncryptUtil.aesEncrypt(content, key);
        System.out.println("加密后：" + encrypt);

        String decrypt = EncryptUtil.aesDecrypt("", key);
        System.out.println("解密后：" + decrypt);
    }
}
