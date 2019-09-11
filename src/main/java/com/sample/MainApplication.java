package com.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EntityScan(basePackageClasses = {
		MainApplication.class
})
public class MainApplication {
	@PostConstruct
	void init() {
		System.out.println("App is started...");
	}

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}
}