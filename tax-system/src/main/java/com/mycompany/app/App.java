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

        Product product = new Product("P-001", "Laptop", "Eletronic", TaxType.ICMS, california.code());

        System.out.println(product);
        System.out.println(california);

    }
}
