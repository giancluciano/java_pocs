package com.mycompany.app;

import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private double balance = 0.0;
    private final ReentrantLock lock = new ReentrantLock();

    public void deposit (double amount) {
        lock.lock();
        try {
            balance += amount;
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(double amount) {
        lock.lock();
        try {
            if (balance >= amount) {
                balance -= amount;
            }
        } finally {
            lock.unlock();
        }
    }
}
