package com.object_store.replication_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableKafka
@EnableAsync
public class Replication_serviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(Replication_serviceApplication.class, args);
	}

}
