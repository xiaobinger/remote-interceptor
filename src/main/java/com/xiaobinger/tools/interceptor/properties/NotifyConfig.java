package com.xiaobinger.tools.interceptor.properties;

import lombok.Data;

/**
 * @author xiongbing
 * @date 2025/11/7 17:20
 * @description
 */
@Data
public class NotifyConfig {

    private String webhookUrl;

    private String template;

    private String title;

    private String msgType;

    private String atMobiles;

    private String secret;
}
