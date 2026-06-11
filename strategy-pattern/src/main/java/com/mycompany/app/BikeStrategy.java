package com.mycompany.app;

public record BikeStrategy() implements Strategy {
    public int execute(int distance) {
        return distance * 1;
    }
}
