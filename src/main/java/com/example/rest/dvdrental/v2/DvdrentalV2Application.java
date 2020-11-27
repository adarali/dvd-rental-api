package com.example.rest.dvdrental.v2;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.time.Duration;

@SpringBootApplication
@EnableCaching
public class DvdrentalV2Application {

	public static void main(String[] args) {
		SpringApplication.run(DvdrentalV2Application.class, args);
	}

}
