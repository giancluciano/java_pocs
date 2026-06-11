package com.mycompany.app;

public record CarStrategy() implements Strategy {
    public int execute(int distance) {
        return distance * 3;
    }
}
