package com.example.authservice.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessagePublisher {

    private final RabbitTemplate template;

    public MessagePublisher(RabbitTemplate template){
        this.template = template;
    }

    public void publishMessage(String username){

        CustomMessage customMessage = new CustomMessage("User created: " + username);

        template.convertAndSend(MQConfig.EXCHANGE,MQConfig.ROUTING_KEY, customMessage);

    }
}
