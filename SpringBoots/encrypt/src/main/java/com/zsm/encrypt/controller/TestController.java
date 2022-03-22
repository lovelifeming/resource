package com.zsm.encrypt.controller;

import com.zsm.encrypt.common.Crypto;
import com.zsm.encrypt.common.DecryptRequest;
import com.zsm.encrypt.common.EncryptResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: zengsm.
 * @Date:Created in 2020-09-12 10:02.
 * @Description:
 */
@RestController
@RequestMapping("/test/")
@EncryptResponse
@DecryptRequest
public class TestController
{
    @Autowired
    @Qualifier("rrCrypto")
    private Crypto crypto;

    @DecryptRequest(false)
    @EncryptResponse(false)
    @RequestMapping(value = "/enc", method = RequestMethod.POST)
    public String enc(@RequestBody String body)
    {
        return crypto.encrypt(body);
    }

}
