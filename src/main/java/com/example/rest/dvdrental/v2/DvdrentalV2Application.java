package com.example.rest.dvdrental.v2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DvdrentalV2Application {

	public static void main(String[] args) {
		SpringApplication.run(DvdrentalV2Application.class, args);
	}

}
