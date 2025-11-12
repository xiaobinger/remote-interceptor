package com.xiaobinger.tools.interceptor.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xiongbing
 * @date 2025/11/7 11:05
 * @description
 */
@Data
@Component
@ConfigurationProperties(prefix = "remote.http.interceptor")
@RefreshScope
public class RemoteHttpInterceptorConfig {
    private boolean enable = true;
    private List<String> errorMatches;
    private List<ChannelConfigItem> urlMatchPatterns;
    private NotifyConfig notifyConfig;

    public ChannelConfigItem getChannelConfigItem(String urlMatch) {
        if(urlMatch == null || urlMatch.isEmpty()) {
            return null;
        }
        for (ChannelConfigItem channelConfigItem : urlMatchPatterns) {
            if (urlMatch.contains(channelConfigItem.getUrlMatch())) {
                return channelConfigItem;
            }
        }
        return null;
    }
}
