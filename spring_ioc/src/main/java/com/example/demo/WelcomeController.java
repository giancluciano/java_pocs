package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WelcomeController {
    private final GreetingService greetingService;

    @Autowired
    public WelcomeController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    public void welcome(String name) {
        String greeting = greetingService.greet(name);
        System.out.println(greeting);
    }
}
