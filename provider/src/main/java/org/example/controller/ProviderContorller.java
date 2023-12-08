package org.example.controller;

import org.example.constant.MqConstant;
import org.example.vo.ReqBody;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/mq")
public class ProviderContorller {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping(value = "/w2baseq")
    public ResponseEntity<String> writeBaseQueue(@RequestBody ReqBody body){
        rabbitTemplate.convertAndSend(MqConstant.BaseQueue, body.getMessage());

        return new ResponseEntity<>("send Ok", HttpStatus.OK);
    }

    @PostMapping(value = "/w2fanout")
    public ResponseEntity<String> writeFanoutQueue(@RequestBody ReqBody body){
        rabbitTemplate.convertAndSend(MqConstant.FanoutExchange,"", body.getMessage());

        return new ResponseEntity<>("send Ok", HttpStatus.OK);
    }

    @PostMapping(value = "/w2direct")
    public ResponseEntity<String> writeDirectQueue(@RequestBody ReqBody body){
        rabbitTemplate.convertAndSend(MqConstant.DirectExchange,MqConstant.DirectRouting, body.getMessage());

        return new ResponseEntity<>("send Ok", HttpStatus.OK);
    }

    @PostMapping(value = "/w2directack")
    public ResponseEntity<String> writeDirectQueueAck(@RequestBody ReqBody body){
        // 準備 CorrelationData 設置消息 ID
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(MqConstant.DirectExchange,MqConstant.DirectRouting, body.getMessage(), correlationData);

        return new ResponseEntity<>("send Ok", HttpStatus.OK);
    }
}
