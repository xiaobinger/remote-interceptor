package com.xiaobinger.tools.interceptor.core;

import cn.hutool.extra.spring.SpringUtil;
import com.xiaobinger.tools.interceptor.properties.ChannelConfigItem;
import com.xiaobinger.tools.interceptor.properties.NotifyConfig;
import com.xiaobinger.tools.interceptor.utils.DingDingMessageSendUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiongbing
 * @date 2025/11/12 11:12
 * @description
 */
@Slf4j
public class RemoteBaseHttpInterceptor {
    protected String processBody(ChannelConfigItem channelConfigItem, String body) {
        boolean needDecrypt = channelConfigItem.isNeedDecrypt();
        body = !needDecrypt ? body : SpringUtil.getBean(HttpBodyProcessor.class).processBody(body,
                channelConfigItem.getChannelType(), channelConfigItem.getDecryptKey(),
                channelConfigItem.getDecryptField());
        return body;
    }

    protected static void triggerNotify(URL urlInfo, String body,
                                        ChannelConfigItem channelConfigItem,
                                        NotifyConfig notifyConfig) {
        if (notifyConfig == null) {
            return;
        }
        String host = urlInfo.getHost();
        String path = urlInfo.getPath();
        int port = urlInfo.getPort();
        String baseAddress = urlInfo.getProtocol() + "://" + host + (port == -1 ? "" : ":" + port);
        log.info("远程调用地址：{}{}", baseAddress, path);
        log.info("远程调用结果：{}", body);
        List<String> contents = new ArrayList<>();
        contents.add(channelConfigItem.getChannelName());
        contents.add(baseAddress);
        contents.add(path);
        contents.add(body);
        DingDingMessageSendUtils.send(notifyConfig,contents);
    }


}
