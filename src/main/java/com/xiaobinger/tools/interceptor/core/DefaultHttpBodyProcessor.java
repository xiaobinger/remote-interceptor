package com.xiaobinger.tools.interceptor.core;

/**
 * @author xiongbing
 * @date 2025/11/12 9:43
 * @description 默认的http响应体处理器
 */
public class DefaultHttpBodyProcessor implements HttpBodyProcessor {
    @Override
    public String processBody(String body, String channelType, String decryptKey, String decryptField) {
        return body;
    }
}
