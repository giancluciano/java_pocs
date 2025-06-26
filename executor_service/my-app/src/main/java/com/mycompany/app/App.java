package com.mycompany.app;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // submit tasks

        try {
            for(int i = 0; i <=6; i++){
                final int id = i;
                executor.submit(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("Task "+ id + " executed by " + Thread.currentThread().getName());
                });
            }

            // submit tasks and get results
            List<Future<String>> futures = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                final int id = i;
                Callable<String> task = () -> {
                    Thread.sleep(1000);
                    return "Result from" + id;
                };
                Future<String> future = executor.submit(task);
                futures.add(future);
            }

            for (Future<String> future : futures) {
                try {
                    String result = future.get();
                    System.out.println(result);
                } catch (ExecutionException | InterruptedException e) {
                    System.err.println("error");
                }
            }

        } finally {
            System.out.println("Done");
            executor.shutdown();

            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                    if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                        System.err.println("Error");
                    }
                }

            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
