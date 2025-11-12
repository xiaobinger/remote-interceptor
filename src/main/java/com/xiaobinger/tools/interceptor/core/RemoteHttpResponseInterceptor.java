package com.xiaobinger.tools.interceptor.core;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpConnection;
import cn.hutool.http.HttpInterceptor;
import cn.hutool.http.HttpResponse;
import com.xiaobinger.tools.interceptor.properties.ChannelConfigItem;
import com.xiaobinger.tools.interceptor.properties.RemoteHttpInterceptorConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author xiongbing
 * @date 2025/11/6 17:38
 * @description
 */
@Slf4j
@AllArgsConstructor
public class RemoteHttpResponseInterceptor extends RemoteBaseHttpInterceptor implements HttpInterceptor<HttpResponse> {
    private final RemoteHttpInterceptorConfig remoteHttpInterceptorConfig;
    /**
     * @param httpResponse 响应对象
     */
    @Override
    public void process(HttpResponse httpResponse) {
        //拦截器不要影响主业务流程
        try {
            if (!remoteHttpInterceptorConfig.isEnable()) {
                return;
            }
            // 处理响应
            HttpConnection httpConnection = (HttpConnection)BeanUtil.getFieldValue(httpResponse, "httpConnection");
            String path = httpConnection.getUrl().getPath();
            String host = httpConnection.getUrl().getHost();
            String body = httpResponse.body();
            if (!CommonUtils.isNotBlank(host) || host.contains("oapi.dingtalk.com")) {
                return;
            }
            ChannelConfigItem channelConfigItem = remoteHttpInterceptorConfig.getChannelConfigItem(host);
            if (channelConfigItem == null) {
                return;
            }
            if (!channelConfigItem.isFilterResponse()){
                return;
            }
            body = processBody(channelConfigItem, body);
            List<String> errorMatches = remoteHttpInterceptorConfig.getErrorMatches();
            boolean notifyFlag = CommonUtils.containsAny(body,errorMatches.toArray(new String[0]));
            if (!httpResponse.isOk() || notifyFlag){
                triggerNotify(host, path, body, channelConfigItem,
                        remoteHttpInterceptorConfig.getNotifyConfig());
            }
        } catch (Exception e) {
            log.error("远程调用拦截器异常！", e);
        }
    }


}
