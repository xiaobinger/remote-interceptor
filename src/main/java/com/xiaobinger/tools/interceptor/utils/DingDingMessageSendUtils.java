package com.xiaobinger.tools.interceptor.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.xiaobinger.tools.interceptor.properties.NotifyConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author xiongbing
 * @date 2023/3/11 14:26
 * @description
 */
@Slf4j
public class DingDingMessageSendUtils {

    public static void send(NotifyConfig notifyConfig, List<String> contents){
        //钉钉请求参数
        Map<String,Object> param = new HashMap<>(3);
        //固定markdown格式
        param.put("msgtype",notifyConfig.getMsgType());
        Map<String,String> msgBody = new HashMap<>(2);
        msgBody.put("title",notifyConfig.getTitle());
        StringBuffer text = new StringBuffer(messageFormat(notifyConfig.getTemplate(),contents));
        Map<String,Object> at = new HashMap<>(1);
        List<String> atMobiles = Collections.singletonList(notifyConfig.getAtMobiles());
        at.put("atMobiles", atMobiles);
        atMobiles.forEach(phone -> {
            if ("markdown".equals(notifyConfig.getMsgType())) {
                text.append("<font color=\"#005eff\" >@").append(phone).append("</font>");
            } else {
                text.append("@").append(phone);
            }
        });
        param.put("at",at);
        switch (notifyConfig.getMsgType()) {
            case "markdown" :
                //markdown内容体
                param.put("markdown",msgBody);
                msgBody.put("text",text.toString());
                break;
            case "text" :
                //文本内容体
                param.put("text",msgBody);
                msgBody.put("content",text.toString());
                break;
            default:break;
        }
        String json = HttpUtil.post(notifyConfig.getWebhookUrl()
                + DingDingSignUtil.sign(notifyConfig.getSecret()), JSONUtil.toJsonStr(param));
        log.info("钉钉群消息机器人消息通知响应：{}", JSONUtil.toJsonStr(json));
    }

    public static String messageFormat(String template,List<String> content){
        String text = template;
        if (CollectionUtil.isNotEmpty(content)) {
            for (int i=0;i<content.size();i++) {
                String contentIndex = Optional.ofNullable(content.get(i)).orElse("");
                text=text.replace("{"+i+"}",contentIndex);
            }
        }
        return text;
    }

}
