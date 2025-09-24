package com.olajuwon.qm.socket_server;

import com.olajuwon.qm.socket_server.Config.SpringConfigurator;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@SpringBootApplication
@EnableScheduling
public class SocketServerApplication implements ApplicationContextAware {

	public static void main(String[] args) {
		SpringApplication.run(SocketServerApplication.class, args);
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringConfigurator.setApplicationContext(applicationContext);
	}
}
