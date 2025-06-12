package com.mycompany.app;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        int sum = numbers.stream()
                         .mapToInt(Integer::intValue)
                         .sum();
        System.out.println("list sum : " + sum);

        int even_sum = numbers.stream()
                        .filter(n -> n % 2 == 0)
                        .mapToInt(Integer::intValue)
                        .sum();
        System.out.println("even numbers sum : " + even_sum);

        List<Integer> squared = numbers.stream()
                                    .map(n -> n * 2)
                                    .collect(Collectors.toList());
        System.out.println("Squared list " + squared);

        List<String> names = Arrays.asList("jon", "jesse");

        List<String> uppercase = names.stream()
                                    .map(String::toUpperCase)
                                    .collect(Collectors.toList());
        System.out.println("uppercase names: " + uppercase);

        // working with objects
        List<Person> people = Arrays.asList(new Person("jon"));
        List<String> people_names = people.stream()
                                .map(Person::getName)
                                .map(name -> "person: " + name)
                                .collect(Collectors.toList());
        System.out.println(people_names);

        Predicate<Integer> isOdd = n -> n % 2 == 1;
        List<Integer> odds = numbers.stream()
                                .filter(isOdd)
                                .collect(Collectors.toList());
        System.out.println(odds);

    }
}
