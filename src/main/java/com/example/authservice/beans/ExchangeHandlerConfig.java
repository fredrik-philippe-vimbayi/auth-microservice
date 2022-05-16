package com.example.authservice.beans;

import com.example.authservice.exchange.ExchangeHandler;
import com.example.authservice.mapper.UserMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ExchangeHandlerConfig {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ExchangeHandler exchangeHandler(RabbitTemplate rabbitTemplate, UserMapper userMapper) {
        return new ExchangeHandler(rabbitTemplate, userMapper);
    }
}
