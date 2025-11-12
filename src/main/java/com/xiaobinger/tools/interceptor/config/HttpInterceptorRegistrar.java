package com.xiaobinger.tools.interceptor.config;

import cn.hutool.http.GlobalInterceptor;
import com.xiaobinger.tools.interceptor.core.DefaultHttpBodyProcessor;
import com.xiaobinger.tools.interceptor.core.HttpBodyProcessor;
import com.xiaobinger.tools.interceptor.core.RemoteHttpRequestInterceptor;
import com.xiaobinger.tools.interceptor.core.RemoteHttpResponseInterceptor;
import com.xiaobinger.tools.interceptor.properties.RemoteHttpInterceptorConfig;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author xiongbing
 * @date 2025/11/6 19:24
 * @description
 */
@EnableConfigurationProperties(RemoteHttpInterceptorConfig.class)
@Configuration
@ConditionalOnProperty(prefix = "remote.http.interceptor", name = "enable", havingValue = "true")
public class HttpInterceptorRegistrar implements ApplicationRunner {
    @Resource
    private RemoteHttpInterceptorConfig remoteHttpInterceptorConfig;
    @Override
    public void run(ApplicationArguments args) {
        GlobalInterceptor.INSTANCE.addRequestInterceptor(new RemoteHttpRequestInterceptor(remoteHttpInterceptorConfig));
        GlobalInterceptor.INSTANCE.addResponseInterceptor(new RemoteHttpResponseInterceptor(remoteHttpInterceptorConfig));
    }

    @Bean
    @ConditionalOnMissingBean(HttpBodyProcessor.class)
    public HttpBodyProcessor httpBodyProcessor() {
        return new DefaultHttpBodyProcessor();
    }
}
