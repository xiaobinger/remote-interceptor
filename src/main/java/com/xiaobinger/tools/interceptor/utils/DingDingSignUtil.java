package com.xiaobinger.tools.interceptor.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author xiongbing
 * @date 2023/3/10 16:53
 * @description
 */
public class DingDingSignUtil {


    public static String sign(String secret){
        try{
            Long timestamp = System.currentTimeMillis();
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            String sign = URLEncoder.encode(Base64.getEncoder().encodeToString(signData), StandardCharsets.UTF_8.name());
            return transfer(sign,timestamp);
        } catch (Exception e){
            return null;
        }
    }

    public static String transfer(String sign,Long timestamp) {
        return "&" + "sign=" + sign + "&" + "timestamp=" + timestamp;
    }

}
