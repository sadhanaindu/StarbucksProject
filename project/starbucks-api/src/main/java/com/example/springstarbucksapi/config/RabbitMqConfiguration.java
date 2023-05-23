package com.example.springstarbucksapi.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {

    @Bean
    public Queue starbucks() {
        return new Queue("starbucks");
    }
}


