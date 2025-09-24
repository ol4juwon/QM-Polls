package com.olajuwon.qm.polls_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
public class PollsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PollsServiceApplication.class, args);
	}

}
