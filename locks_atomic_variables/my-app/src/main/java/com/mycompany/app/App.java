package com.mycompany.app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        AtomicInteger counter = new AtomicInteger();

        double[] operations = {100, -50};
        BankAccount account = new BankAccount();

        try {

            for (double i : operations) {
                if (i > 0){
                    executor.submit(() -> {
                        account.withdraw(Math.abs(i));
                        counter.incrementAndGet();
                    });
                } else {
                    executor.submit(() -> {
                        account.deposit(Math.abs(i));
                        counter.incrementAndGet();
                    });
                }
            }
        } finally {
            executor.shutdown();
        }
    }
}
