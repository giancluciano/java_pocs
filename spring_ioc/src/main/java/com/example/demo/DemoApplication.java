package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(DemoApplication.class, args);
		WelcomeController welcomeController = context.getBean(WelcomeController.class);
		welcomeController.welcome("Jon");

		WelcomeController anotherReference = context.getBean(WelcomeController.class);
        System.out.println("Same instance? " + (welcomeController == anotherReference));

	}

}
