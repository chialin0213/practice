package org.example.conf;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.example.constant.MqConstant;

@Configuration
public class QueueConfig {
    @Bean
    public Queue baseQueue(){
        return new Queue(MqConstant.BaseQueue);
    }

    @Bean
    public Queue fanoutQueue(){
        return new Queue(MqConstant.FanoutQueue);
    }

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(MqConstant.FanoutExchange);
    }

    @Bean
    public Binding fanoutBinding(){
        return BindingBuilder.bind(fanoutQueue()).to(fanoutExchange());
    }

    @Bean
    public Queue errorQueue() {
        return new Queue(MqConstant.ErrQueue);
    }

    @Bean
    public DirectExchange errorExchange(){
        return new DirectExchange(MqConstant.ErrExchange);
    }

    @Bean
    public Binding errorBinding(){
        return BindingBuilder.bind(errorQueue()).to(errorExchange()).with(MqConstant.ErrRouting);
    }

    /**
     * 處理 RabbitMQ 消息異常的 Spring Bean，它使用 RepublishMessageRecoverer 類
     * 將發送失敗的消息重新發送到指定的交換機和路由鍵，以避免消息丢失
     */
    @Bean
    public MessageRecoverer recoverer(RabbitTemplate rabbitTemplate){
        return new RepublishMessageRecoverer(rabbitTemplate, MqConstant.ErrExchange, MqConstant.ErrRouting);
    }
}