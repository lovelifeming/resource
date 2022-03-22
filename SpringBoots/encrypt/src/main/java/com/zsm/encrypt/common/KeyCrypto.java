package com.zsm.encrypt.common;

/**
 * @Author: zengsm.
 * @Date:Created in 2021-04-06 22:31.
 * @Description:
 */
public interface KeyCrypto extends Crypto
{
    /**
     * 设置key,并返回自己
     * @param key key
     * @return this
     */
    KeyCrypto setKey(String key);
}
