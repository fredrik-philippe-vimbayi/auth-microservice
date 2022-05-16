package com.example.authservice.beans.exchange;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.authservice.exchange.ExchangeConstants.TOPIC_EXCHANGE_NAME;

@Configuration
public class TopicExchangeConfig {

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

}
