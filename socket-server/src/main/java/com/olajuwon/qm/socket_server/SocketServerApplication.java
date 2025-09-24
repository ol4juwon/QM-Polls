package com.olajuwon.qm.socket_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@SpringBootApplication
@EnableScheduling
public class SocketServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocketServerApplication.class, args);
	}

}
