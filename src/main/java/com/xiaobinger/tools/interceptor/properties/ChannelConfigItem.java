package com.xiaobinger.tools.interceptor.properties;

import lombok.Data;

/**
 * @author xiongbing
 * @date 2025/11/7 14:58
 * @description
 */
@Data
public class ChannelConfigItem {

    private String urlMatch;

    private String channelName;

    private String channelType;

    private boolean  needDecrypt;

    private String decryptField;

    private String decryptKey;

    private boolean filterRequest;

    private boolean filterResponse;

}
