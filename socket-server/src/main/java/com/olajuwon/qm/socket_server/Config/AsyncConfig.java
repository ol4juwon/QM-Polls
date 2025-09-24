package com.olajuwon.qm.socket_server.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "pollExecutor")
    public Executor pollExecutor() {
        return Executors.newFixedThreadPool(10); // adjust pool size
    }
}