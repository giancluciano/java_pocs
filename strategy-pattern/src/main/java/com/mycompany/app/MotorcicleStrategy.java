package com.mycompany.app;

public record MotorcicleStrategy() implements Strategy {
 public int execute(int distance) {
    return distance * 2;
 }
}
