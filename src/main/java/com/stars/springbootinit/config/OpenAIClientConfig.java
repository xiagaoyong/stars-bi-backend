package com.stars.springbootinit.config;

import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.function.KeyRandomStrategy;
import com.unfbx.chatgpt.interceptor.OpenAiResponseInterceptor;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author Prometheus
 * @description
 * @createDate 2023/09/24 0024
 */
@Configuration
public class OpenAIClientConfig {
    @Bean
    public OpenAiClient openAiClient(){
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                //.proxy(proxy)//自定义代理
                //.addInterceptor(httpLoggingInterceptor)//自定义日志输出
                .addInterceptor(new OpenAiResponseInterceptor())//自定义返回值拦截
                .connectTimeout(10, TimeUnit.MINUTES)//自定义超时时间
                .writeTimeout(10, TimeUnit.MINUTES)//自定义超时时间
                .readTimeout(10, TimeUnit.MINUTES)//自定义超时时间
                .build();
        return OpenAiClient.builder()
                //添加自己的密钥
                .apiKey(Arrays.asList("sk-xxx"))
                //随机策略
                .keyStrategy(new KeyRandomStrategy())
                //如果是国内的,自己做了代理就传代理地址,没有就用我这个
                .apiHost("https://gpt-test.wake7720.top/")
                .okHttpClient(okHttpClient)
                .build();
    }
}
