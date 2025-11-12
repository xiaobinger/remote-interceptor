package com.xiaobinger.tools.interceptor.core;

/**
 * @author xiongbing
 * @date 2025/11/12 9:37
 * @description 响应体处理器
 */
public interface HttpBodyProcessor {
    /**
     * 处理响应体
     * @param body 响应体
     * @param channelType 接口类别
     * @param decryptKey 解密密钥
     * @param decryptField 解密字段
     * @return 处理后的响应体
     */
    String processBody(String body, String channelType, String decryptKey, String decryptField);
}
