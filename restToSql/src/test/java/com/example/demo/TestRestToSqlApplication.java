package com.example.demo;

import org.springframework.boot.SpringApplication;

public class TestRestToSqlApplication {

	public static void main(String[] args) {
		SpringApplication.from(RestToSqlApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
