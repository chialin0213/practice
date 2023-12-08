package org.example.conf;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * 開啟消息退回機制，在 RabbitTemplate 對象中可以設置一個 ReturnsCallback 回調來處理退回的消息。
 * 該回調函數被調用時，會將消息原樣返回到生產者
 *
 * RabbitTemplate只能設定一次 setConfirmCallback/setReturnsCallback，
 * 無法在controller每次request都設定一次
 */
@Configuration
//從已有的spring上下文取得已實例化的bean
public class RabbitMQConfig implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RabbitTemplate bean = applicationContext.getBean(RabbitTemplate.class);
        bean.setReturnsCallback(r -> {
            System.out.println(String.format("消息發送佇列失敗，回應碼: %s,失敗原因: %s,交換機: %s,路由 key: %s,消息: %s",
                    r.getReplyCode(),r.getReplyText(),r.getExchange(),r.getRoutingKey(),r.getMessage())
            );
        });

        bean.setConfirmCallback((data, ack, caus) -> {
            System.out.println("ack : " + ack + ", 消息id : "+ (data != null ? data.getId() != null ? data.getId() : "" : ""));
            if (!ack) {
                System.out.println("異常 : " + caus);
            }
            System.out.println();
        });
    }
}
