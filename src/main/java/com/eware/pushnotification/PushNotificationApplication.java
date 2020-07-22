package com.eware.pushnotification;

import com.eware.pushnotification.service.Processor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
@EnableBinding(Processor.class)
public class PushNotificationApplication {
	public static void main(String[] args) {
		SpringApplication.run(PushNotificationApplication.class, args);
	}
}
