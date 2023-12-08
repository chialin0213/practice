package org.example.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.example.constant.MqConstant;

@Component
public class QueueListener {
    @RabbitListener(queues = MqConstant.BaseQueue)
    public void baseQueue(String msg){
        System.out.println("baseQueue：【" + msg  + "】");
    }

    @RabbitListener(queues = MqConstant.FanoutQueue)
    public void fanoutQueue(String msg){
        System.out.println("fanoutQueue：【" + msg  + "】");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MqConstant.FanoutQueue1),
            exchange = @Exchange(name = MqConstant.FanoutExchange,type = ExchangeTypes.FANOUT)
    ))
    public void fanoutQueue1(String msg){
        System.out.println("fanoutQueue1：【" + msg  + "】");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MqConstant.DirectQueue),
            exchange = @Exchange(name = MqConstant.DirectExchange, type = ExchangeTypes.DIRECT),
            key = {MqConstant.DirectRouting}
    ))
    public void directQueue(String msg){
        System.out.println("directQueue :【" + msg  + "】");
    }
}
