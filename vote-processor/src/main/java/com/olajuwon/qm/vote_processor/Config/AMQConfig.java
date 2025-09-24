package com.olajuwon.qm.vote_processor.Config;

import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.AllowedListDeserializingMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
@Configuration
public class AMQConfig {


    @Bean
    public MessageConverter messageConverter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        converter.setAllowedListPatterns(
                java.util.List.of("java.util.*", "com.olajuwon.qm.vote_publisher.Entity.*")
        );
        return converter;
    }
}