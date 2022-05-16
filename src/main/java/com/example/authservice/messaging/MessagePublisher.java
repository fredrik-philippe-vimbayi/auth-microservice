package com.example.authservice.messaging;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
public class MessagePublisher {

    private final RabbitTemplate template;

    public MessagePublisher(RabbitTemplate template){
        this.template = template;
    }

    public void publishMessage(String username){

        CustomMessage customMessage = new CustomMessage(UUID.randomUUID().toString(),
                "User created: " + username, new Date());

        template.convertAndSend(MQConfig.EXCHANGE,MQConfig.ROUTING_KEY, customMessage);

    }
}
