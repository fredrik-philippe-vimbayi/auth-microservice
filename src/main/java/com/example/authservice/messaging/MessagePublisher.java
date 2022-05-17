package com.example.authservice.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessagePublisher {

    private final RabbitTemplate template;

    public MessagePublisher(RabbitTemplate template){
        this.template = template;
    }

    public void publishMessage(String username){

        CustomMessage customMessage = new CustomMessage("User created: " + username);

        template.convertAndSend(MQConfig.exchange,MQConfig.routing_key, customMessage);

    }
}
