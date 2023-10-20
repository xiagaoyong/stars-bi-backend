package com.stars.springbootinit.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Prometheus
 * @description
 * @createDate 2023/09/20 0020
 */
@Configuration
public class DelayedConfig {


    public static final String BI_DELAYED_QUEUE = "bi_delayed_queue";
    public static final String BI_DELAYED_EXCHANGE = "bi_delayed_exchange";
    public static final String BI_DELAYED_ROUTING_KEY = "bi_delayed_routing_key";

    public static final String X_DELAYED_MESSAGE = "x-delayed-message";
    public static final String  X_DELAYED_TYPE = "x-delayed-type";
    public static final String  DIRECT = "direct";

    @Bean
    public Queue biDelayedQueue(){
        return QueueBuilder.durable(BI_DELAYED_QUEUE).maxLength(10).build();
    }

    @Bean
    public CustomExchange biDelayedExchange(){
        Map<String, Object> arguments = new HashMap<>(1);
        arguments.put(X_DELAYED_TYPE,DIRECT);
        /*
         * 1. 交换机的名称
         * 2. 交换机的类型
         * 3. 是否持久化
         * 4. 是否自动删除
         * 5. 其他参数
         */
        return new CustomExchange(BI_DELAYED_EXCHANGE,X_DELAYED_MESSAGE,true,false,arguments);
    }

    /**
     *  绑定
     * @param biDelayedQueue
     * @param biDelayedExchange
     * @return
     */

    @Bean
    public Binding biDelayedQueueBindBiDelayedExchange(Queue biDelayedQueue,CustomExchange biDelayedExchange){
        return BindingBuilder.bind(biDelayedQueue).to(biDelayedExchange).with(BI_DELAYED_ROUTING_KEY).noargs();
    }




}
