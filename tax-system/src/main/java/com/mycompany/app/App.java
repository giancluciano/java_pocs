package com.mycompany.app;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;

public class App {
    public static void main(String[] args) {
        State california = new State("CA", "California");

        TaxRegistry registry = new TaxRegistry(List.of(
                new Tax(TaxType.ICMS, california.code(), Year.of(2026), new BigDecimal("0.0725")),
                new Tax(TaxType.PIS, california.code(), Year.of(2026), new BigDecimal("0.0165"))
        ));

        Product product = new Product("P-001", "Laptop", "Eletronic", new BigDecimal("1299.99"), TaxType.ICMS, california.code());

        Tax tax = registry.find(product.taxKey())
                .orElseThrow(() -> new IllegalStateException("No tax for " + product.taxKey()));

        System.out.println(product.describe(tax));

        Product book = new Product("P-002", "Clean Code", "Book", new BigDecimal("49.90"), TaxType.PIS, california.code());

        Tax bookTax = registry.find(book.taxKey())
                .orElseThrow(() -> new IllegalStateException("No tax for " + book.taxKey()));

        System.out.println(book.describe(bookTax));
        System.out.println(california);

    }
}
