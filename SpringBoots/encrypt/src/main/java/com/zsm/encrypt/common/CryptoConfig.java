package com.zsm.encrypt.common;

/**
 * @Author: zengsm.
 * @Date:Created in 2021-04-06 22:17.
 * @Description:
 */


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Request-Response加解密体系的加解密方式
 */
@Configuration
public class CryptoConfig
{
    /**
     * 加密解密方式使用一样的
     */
    @Bean("rrCrypto")
    public Crypto rrCrypto(){
        return new AesCrypto("密钥key");
    }
}
