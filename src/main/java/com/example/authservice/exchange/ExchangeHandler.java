package com.example.authservice.exchange;

import com.example.authservice.dto.NewUser;
import com.example.authservice.mapper.UserMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static com.example.authservice.exchange.ExchangeConstants.ROUTING_PATTERN;
import static com.example.authservice.exchange.ExchangeConstants.TOPIC_EXCHANGE_NAME;

public class ExchangeHandler {

    private final RabbitTemplate rabbitTemplate;
    private final UserMapper userMapper;

    public ExchangeHandler(RabbitTemplate rabbitTemplate, UserMapper userMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.userMapper = userMapper;
    }

    public void publishNewUser(NewUser user) {
        String payload = userMapper.userToJson(user);
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE_NAME, ROUTING_PATTERN, payload);
    }
}
