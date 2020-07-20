package com.eware.startup;

import com.eware.startup.service.Processor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableBinding(Processor.class)
public class PushNotificationApplication {
	public static void main(String[] args) {
		SpringApplication.run(PushNotificationApplication.class, args);
	}
}
