package com.mycompany.app;

import java.math.BigDecimal;
import java.time.Year;

public class App {
    public static void main(String[] args) {
        Product product = new Product("P-001", "Coffee Beans", "Grocery");
        State state = new State("CA", "California");
        Tax tax = new Tax(product.id(), state.code(), Year.of(2026), new BigDecimal("0.0725"));

        System.out.println(product);
        System.out.println(state);
        System.out.println(tax);
    }
}
