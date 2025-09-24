package com.olajuwon.qm.vote_publisher.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "poll-exchange";
    public static final String QUEUE = "vote-queue";
    public static final String ROUTING_KEY = "vote";

    @Bean
    public DirectExchange exchange() { return new DirectExchange(EXCHANGE, true, false); }

    @Bean
    public Queue queue() { return new Queue(QUEUE, true); }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
}