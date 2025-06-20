package com.mycompany.app;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        // supplier example
        Supplier<String> stringSupplier = () -> "Hello";
        System.out.println(stringSupplier.get());

        // supplier with random values
        Supplier<Integer> randomInt = () -> new Random().nextInt(100);
        
        Stream.generate(randomInt).limit(5).forEach(System.out::println);

        // Consumer example

        List<String> names = Arrays.asList("jon", "alice");
        Consumer<String> printer = System.out::println;

        names.stream().forEach(printer);


        List<Integer> numbers = Arrays.asList(1,2,3);

        BinaryOperator<Integer> addition = (a, b) -> a + b;
        int sum = numbers.stream().reduce(0, addition);

        BinaryOperator<Integer> findMax = (a, b) -> a >= b ? a : b;
        int max = numbers.stream().reduce(0, findMax);

        UnaryOperator<String> addPrefix = s -> "Name: " + s;

        names.stream().map(addPrefix).forEach(printer);
    }
}
