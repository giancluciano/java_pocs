package com.mycompany.app;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<Integer> mathTask = () -> {
            Thread.sleep(2000);
            return 2*2;
        };

        try {
            Future<Integer> mathFuture = executor.submit(mathTask);

            System.out.println("math task started, is done: "+ mathFuture.isDone()); // no blocking

            int mathResult = mathFuture.get(); // blocking

            System.out.println("result: "+mathResult);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
