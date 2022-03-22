package com.zsm.encrypt.common;

/**
 * @Author: zengsm.
 * @Date:Created in 2021-04-06 22:44.
 * @Description:
 */
public class CryptoException extends RuntimeException
{
    public CryptoException(String message)
    {
        super(message);
    }

    public CryptoException(Exception e)
    {
        super(e);
    }
}
