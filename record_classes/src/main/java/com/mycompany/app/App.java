package com.mycompany.app;

import java.math.BigDecimal;

public class App {

    public record State(String name, String code) {}

    public record Tax(State state, BigDecimal rate) {}

    public record Product(String name, BigDecimal price, Tax tax) {
        public BigDecimal totalPrice() {
            return price.add(price.multiply(tax.rate()));
        }
    }

    public static void main(String[] args) {
        var california = new State("California", "CA");
        var salesTax = new Tax(california, new BigDecimal("0.0725"));
        var book = new Product("book", new BigDecimal("45.00"), salesTax);

        System.out.println(book);
        System.out.println("Total: " + book.totalPrice());
    }
}
