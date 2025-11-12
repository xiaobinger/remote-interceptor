package com.xiaobinger.tools.interceptor.core;

import cn.hutool.http.HttpInterceptor;
import cn.hutool.http.HttpRequest;
import com.xiaobinger.tools.interceptor.properties.ChannelConfigItem;
import com.xiaobinger.tools.interceptor.properties.RemoteHttpInterceptorConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * @author xiongbing
 * @date 2025/11/6 17:38
 * @description
 */
@Slf4j
@AllArgsConstructor
public class RemoteHttpRequestInterceptor extends RemoteBaseHttpInterceptor implements HttpInterceptor<HttpRequest> {
    private final RemoteHttpInterceptorConfig remoteHttpInterceptorConfig;
    /**
     * @param httpRequest 响应对象
     */
    @Override
    public void process(HttpRequest httpRequest) {
        //拦截器不要影响主业务流程
        try {
            if (!remoteHttpInterceptorConfig.isEnable()) {
                return;
            }
            // 处理响应
            URL url = new URL(httpRequest.getUrl());
            String path = Optional.ofNullable(url.getPath()).orElse("");
            String host = Optional.ofNullable(url.getHost()).orElse("");
            String body = new String(httpRequest.bodyBytes(), StandardCharsets.UTF_8);
            if (!CommonUtils.isNotBlank(host) || host.contains("oapi.dingtalk.com")) {
                return;
            }
            ChannelConfigItem channelConfigItem = remoteHttpInterceptorConfig.getChannelConfigItem(path);
            if (channelConfigItem == null) {
                return;
            }
            if (!channelConfigItem.isFilterRequest()){
                return;
            }
            body = processBody(channelConfigItem, body);
            List<String> errorMatches = remoteHttpInterceptorConfig.getErrorMatches();
            boolean notifyFlag = CommonUtils.containsAny(body,errorMatches.toArray(new String[0]));
            if (notifyFlag){
                triggerNotify(host, path, body, channelConfigItem,
                        remoteHttpInterceptorConfig.getNotifyConfig());
            }
        } catch (Exception e) {
            log.error("远程调用拦截器异常！", e);
        }
    }



}
