package com.zsm.encrypt.common;

import com.zsm.encrypt.util.IoUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * @Author: zengsm.
 * @Date:Created in 2021-04-06 22:07.
 * @Description:
 */
public class DecryptHttpInputMessage implements HttpInputMessage
{
    private HttpInputMessage inputMessage;
    private String charset;
    private Crypto crypto;

    public DecryptHttpInputMessage(HttpInputMessage inputMessage, String charset , Crypto crypto) {
        this.inputMessage = inputMessage;
        this.charset = charset;
        this.crypto = crypto;
    }

    @Override
    public InputStream getBody() throws IOException {
        String content = IoUtil.read(inputMessage.getBody() , charset);

        String decryptBody = crypto.decrypt(content, charset);

        return new ByteArrayInputStream(decryptBody.getBytes(charset));
    }

    @Override
    public HttpHeaders getHeaders() {
        return inputMessage.getHeaders();
    }
}
