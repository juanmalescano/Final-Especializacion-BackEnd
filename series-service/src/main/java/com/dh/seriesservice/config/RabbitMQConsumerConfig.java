package com.dh.seriesservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConsumerConfig {

    @Value("${queue.general.name}")
    private String generalQueue;

    @Bean
    public Queue queue() {
        return new Queue(this.generalQueue, true);
    }
}
