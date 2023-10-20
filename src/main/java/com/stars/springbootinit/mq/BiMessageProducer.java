package com.stars.springbootinit.mq;

import com.stars.springbootinit.mq.config.DelayedConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * @author Prometheus
 * @description
 * @createDate 2023/09/18 0018
 */
@Component
@Slf4j
public class BiMessageProducer {
    @Resource
    private RabbitTemplate rabbitTemplate;


    /**
     * 发送消息
     * @param msg 消息
     */
    public void convertAndSend(String msg){
        //确保消息发送失败后可以重新返回到队列中
        //rabbitTemplate.setMandatory(true)

        //保证消息唯一性
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        //发送消息
        log.info("当前时间：{},发送一条信息给延迟队列bi_delayed_queue",new Date());
        rabbitTemplate.convertAndSend(DelayedConfig.BI_DELAYED_EXCHANGE,DelayedConfig.BI_DELAYED_ROUTING_KEY,msg, message -> {
            message.getMessageProperties().setDelay(30000);
            return message;
        },correlationData);
    }
}
