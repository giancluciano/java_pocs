package com.mycompany.app;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;

public class App {
    public static void main(String[] args) {
        State california = new State("CA", "California");
        State newYork = new State("NY", "New York");
        State florida = new State("FL", "Florida");

        TaxRegistry registry = new TaxRegistry(List.of(
                new Tax(TaxType.ICMS, california.code(), Year.of(2026), new BigDecimal("0.06")),
                new Tax(TaxType.ICMS, newYork.code(), Year.of(2026), new BigDecimal("0.1")),
                new Tax(TaxType.ICMS, florida.code(), Year.of(2026), new BigDecimal("0.02")),
                new Tax(TaxType.PIS, california.code(), Year.of(2026), new BigDecimal("0.04")),
                new Tax(TaxType.PIS, newYork.code(), Year.of(2026), new BigDecimal("0.09")),
                new Tax(TaxType.PIS, florida.code(), Year.of(2026), new BigDecimal("0.05"))
        ));

        TaxRules rules = TaxRules.defaults();

        Product product = new Product("P-001", "Laptop", "Eletronic", new BigDecimal("1299.99"), TaxType.ICMS);
        product.calculate(registry, rules);

        Product book = new Product("P-002", "Clean Code", "Book", new BigDecimal("49.90"), TaxType.PIS);
        book.calculate(registry, rules);

        Product pen = new Product("P-003", "Pen", "Stationery", new BigDecimal("5.00"), TaxType.PIS);
        pen.calculate(registry, rules);
    }
}
