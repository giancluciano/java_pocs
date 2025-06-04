package com.mycompany.app;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        // predicate
        Predicate<Integer> isEven = n -> n % 2 == 0;
        System.out.println("isEven predicate: " + isEven.test(2));

        // consumer
        Consumer<String> print = x -> System.out.println(x);
        print.accept("consumer example");

        // custom functional interface
        FunctionalCalc sum = (a,b) -> a + b;
        print.accept("functional calc: " + sum.calc(1, 2));
    }
}
