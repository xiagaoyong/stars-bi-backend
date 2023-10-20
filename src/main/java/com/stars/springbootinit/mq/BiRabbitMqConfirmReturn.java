package com.stars.springbootinit.mq;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author Prometheus
 * @description
 * @createDate 2023/09/19 0019
 */
@Component
@Slf4j
public class BiRabbitMqConfirmReturn implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback  {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }


    /**
     * Confirmation callback.
     *
     * @param correlationData correlation data for the callback.
     * @param ack             true for ack, false for nack
     * @param cause           An optional cause, for nack, when available, otherwise null.
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack){
            log.info("消息正确到达交换机，ack {}",ack);
        }else{
            log.info("消息到达交换机失败 ，ack{}, cause{}",ack,cause);
        }

    }


    /**
     * Returned message callback.
     *
     * @param returned the returned message and metadata.
     */
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.error("消息从交换机路由至队列失败：replyCode:{}, replText:{}, exchange:{}, routingKey:{}"
                ,returned.getReplyCode(),returned.getReplyText(),returned.getExchange(),returned.getRoutingKey());
    }
}
