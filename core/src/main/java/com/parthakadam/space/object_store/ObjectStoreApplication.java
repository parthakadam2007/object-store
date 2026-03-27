package com.parthakadam.space.object_store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ObjectStoreApplication {


	public static void main(String[] args) {
		SpringApplication.run(ObjectStoreApplication.class, args);

	}

}
